package co.anbora.labs.ngrok.runtimes

import co.anbora.labs.ngrok.model.NgrokService


sealed class NgrokServiceRuntime<T>(val service: T, parentRuntime: NgrokApplicationRuntime) :
    NgrokBaseRuntime(service.getName()) where T : NgrokService {



}