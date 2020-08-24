// IPlayerCommand.aidl
package com.adtest.aidl;

import com.adtest.aidl.IProgressListener;
// Declare any non-default types here with import statements

interface IPlayerCommand {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void command(int command);
    void registerProgress(IProgressListener listener);
    void unRegisterProgress(IProgressListener listener);
}
