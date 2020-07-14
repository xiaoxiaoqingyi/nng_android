package com.terminus.sz.nnglibrarydemo;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

public interface NngDelegate extends Library {

    NngDelegate INSTANCE =  Native.load( "native-lib", NngDelegate.class);

    int nng_req0_open(NNGSocket.ByReference nng_socket);
    int nng_rep0_open(NNGSocket.ByReference nng_socket);

    int nng_sub0_open(NNGSocket.ByReference nng_socket);
    int nng_pub0_open(NNGSocket.ByReference nng_socket);

    int nng_dial(NNGSocket.ByValue nng_socket, String url, NNGSocket nng_dialer, int flags);
    int nng_listen(NNGSocket.ByValue nng_socket, String url, NNGSocket nng_listener, int flags);
    int nng_setopt(NNGSocket.ByValue nng_socket, String opt, String val, int valsz);

    int nng_send(NNGSocket.ByValue nng_socket, byte[] msg, int len, int flags);
    int nng_recv(NNGSocket.ByValue nng_socket, byte[] res, IntByReference len, int flags);


    int nng_close(NNGSocket.ByValue nng_socket);
    void nng_free(Pointer pointer, IntByReference size);

}
