package com.mockforge.project.domain;

/**
 * Runtime status of a project.
 */
public enum ProjectStatus {
    /**
     * Project exists but runtime is not active.
     * No requests are being served.
     */
    STOPPED,

    /**
     * Runtime is active and serving requests.
     */
    RUNNING,

    /**
     * Project encountered an error during startup or operation.
     */
    ERROR
}
