package kolomyichuk.runly.service.manager

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

abstract class BaseServiceManager {
    protected val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    open fun cancelScope(){
        scope.cancel()
    }
}
