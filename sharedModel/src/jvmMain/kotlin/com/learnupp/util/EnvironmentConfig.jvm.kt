package com.learnupp.util

actual object EnvironmentConfig {
    /**
     * JVM debug flag:
     * -Dlearnupp.debug=true  (or set env LEARNUPP_DEBUG=true)
     */
    actual val isDebug: Boolean =
        (System.getProperty("learnupp.debug") ?: "").equals("true", ignoreCase = true) ||
            (System.getenv("LEARNUPP_DEBUG") ?: "").equals("true", ignoreCase = true)
}


