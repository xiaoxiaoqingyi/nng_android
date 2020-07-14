#include "include/nng/nng.h"
#include "include/nng/compat/nanomsg/bus.h"
#include "include/nng/compat/nanomsg/inproc.h"
#include "include/nng/compat/nanomsg/ipc.h"
#include "include/nng/compat/nanomsg/nn.h"
#include "include/nng/compat/nanomsg/pair.h"
#include "include/nng/compat/nanomsg/pipeline.h"
#include "include/nng/compat/nanomsg/pubsub.h"
#include "include/nng/compat/nanomsg/reqrep.h"
#include "include/nng/compat/nanomsg/survey.h"
#include "include/nng/compat/nanomsg/tcp.h"
#include "include/nng/compat/nanomsg/ws.h"
#include "include/nng/protocol/bus0/bus.h"
#include "include/nng/protocol/pair0/pair.h"
#include "include/nng/protocol/pair1/pair.h"
#include "include/nng/protocol/pipeline0/pull.h"
#include "include/nng/protocol/pipeline0/push.h"
#include "include/nng/protocol/pubsub0/pub.h"
#include "include/nng/protocol/pubsub0/sub.h"
#include "include/nng/protocol/reqrep0/rep.h"
#include "include/nng/protocol/reqrep0/req.h"
#include "include/nng/protocol/survey0/respond.h"
#include "include/nng/protocol/survey0/survey.h"
#include "include/nng/supplemental/http/http.h"
#include "include/nng/supplemental/tls/engine.h"
#include "include/nng/supplemental/tls/tls.h"
#include "include/nng/supplemental/util/options.h"
#include "include/nng/supplemental/util/platform.h"
#include "include/nng/transport/inproc/inproc.h"
#include "include/nng/transport/ipc/ipc.h"
#include "include/nng/transport/tcp/tcp.h"
#include "include/nng/transport/tls/tls.h"
#include "include/nng/transport/ws/websocket.h"
#include "include/nng/transport/zerotier/zerotier.h"


extern "C"
int client() {
    nng_socket sock;
    int        rv;
    if ((rv = nng_req0_open(&sock)) != 0) {
        return -1;
    }
    const char *url = "tcp://127.0.0.1:4567";
    if ((rv = nng_dial(sock, url, NULL, 0)) != 0) {
        return rv;
    }
    return rv;
}

extern "C"
int server() {
    nng_socket sock;
    int        rv;

    if ((rv = nng_rep0_open(&sock)) != 0) {
        return -1;
    }
    const char *url = "tcp://127.0.0.1:4567";
    if ((rv = nng_listen(sock, url, NULL, 0)) != 0) {
        return rv;
    }
    for (;;) {
        char *   buf = NULL;
        size_t   sz;
        uint64_t val;
        if ((rv = nng_recv(sock, &buf, &sz, NNG_FLAG_ALLOC)) != 0) {
            return rv;
        }
//        if ((sz == sizeof(uint64_t)) &&
//            ((GET64(buf, val)) == DATECMD)) {
//            time_t now;
//            now = time(&now);
//            // Reuse the buffer.  We know it is big enough.
//            PUT64(buf, (uint64_t) now);
//            rv = nng_send(sock, buf, sz, NNG_FLAG_ALLOC);
//            if (rv != 0) {
//                return rv;
//            }
//            continue;
//        }
        if (buf != NULL) {
            char *reply = "response answer";
            rv = nng_send(sock, reply, sizeof(reply), NNG_FLAG_ALLOC);
            if (rv != 0) {
                return rv;
            }
            continue;
        }

        // Unrecognized command, so toss the buffer.
        nng_free(buf, sz);
    }
    return 0;
}