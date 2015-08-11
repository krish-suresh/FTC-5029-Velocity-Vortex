package org.swerverobotics.library.internal;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.*;
import org.swerverobotics.library.interfaces.*;

import java.util.concurrent.locks.Lock;

/**
 * Another in our story..
 */
public class ThunkedI2cController implements I2cController, IThunkingWrapper<I2cController>
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    private I2cController target;          // can only talk to him on the loop thread

    @Override public I2cController getThunkTarget() { return this.target; }

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    private ThunkedI2cController(I2cController target)
        {
        if (target == null) throw new NullPointerException("null " + this.getClass().getSimpleName() + " target");
        this.target = target;
        }

    static public ThunkedI2cController create(I2cController target)
        {
        return target instanceof ThunkedI2cController ? (ThunkedI2cController)target : new ThunkedI2cController(target);
        }

    //----------------------------------------------------------------------------------------------
    // I2cController
    //----------------------------------------------------------------------------------------------

    @Override public void close()
        {
        (new ThunkForWriting()
        {
        @Override protected void actionOnLoopThread()
            {
            target.close();
            }
        }).doUntrackedWriteOperation();
        }

    @Override public int getVersion()
        {
        return (new ThunkForReading<Integer>()
        {
        @Override protected void actionOnLoopThread()
            {
            this.result = target.getVersion();
            }
        }).doUntrackedReadOperation();
        }

    @Override public String getDeviceName()
        {
        return (new ThunkForReading<String>()
        {
        @Override protected void actionOnLoopThread()
            {
            this.result = target.getDeviceName();
            }
        }).doUntrackedReadOperation();
        }

    @Override public SerialNumber getSerialNumber()
        {
        return (new ThunkForReading<SerialNumber>()
        {
        @Override protected void actionOnLoopThread()
            {
            this.result = target.getSerialNumber();
            }
        }).doUntrackedReadOperation();
        }

    @Override public void enableI2cReadMode(final int physicalPort, final int i2cAddress, final int memAddress, final int length)
        {
        (new ThunkForWriting()
        {
        @Override protected void actionOnLoopThread()
            {
            target.enableI2cReadMode(physicalPort, i2cAddress, memAddress, length);
            }
        }).doWriteOperation();
        }

    @Override public void enableI2cWriteMode(final int physicalPort, final int i2cAddress, final int memAddress, final int length)
        {
        (new ThunkForWriting()
        {
        @Override protected void actionOnLoopThread()
            {
            target.enableI2cWriteMode(physicalPort, i2cAddress, memAddress, length);
            }
        }).doWriteOperation();
        }

    @Override public Lock getI2cReadCacheLock(final int physicalPort)
        {
        return (new ThunkForReading<Lock>()
        {
        @Override protected void actionOnLoopThread()
            {
            this.result = target.getI2cReadCacheLock(physicalPort);
            }
        }).doReadOperation();
        }

    @Override public Lock getI2cWriteCacheLock(final int physicalPort)
        {
        return (new ThunkForReading<Lock>()
        {
        @Override protected void actionOnLoopThread()
            {
            this.result = target.getI2cWriteCacheLock(physicalPort);
            }
        }).doReadOperation();
        }

    @Override public byte[] getI2cReadCache(final int physicalPort)
        {
        return (new ThunkForReading<byte[]>()
        {
        @Override protected void actionOnLoopThread()
            {
            this.result = target.getI2cReadCache(physicalPort);
            }
        }).doReadOperation();
        }

    @Override public byte[] getI2cWriteCache(final int physicalPort)
        {
        return (new ThunkForReading<byte[]>()
        {
        @Override protected void actionOnLoopThread()
            {
            this.result = target.getI2cWriteCache(physicalPort);
            }
        }).doReadOperation();
        }

    @Override public void setI2cPortActionFlag(final int physicalPort)
        {
        (new ThunkForWriting()
        {
        @Override protected void actionOnLoopThread()
            {
            target.setI2cPortActionFlag(physicalPort);
            }
        }).doWriteOperation();
        }

    @Override public boolean isI2cPortActionFlagSet(final int physicalPort)
        {
        return (new ThunkForReading<Boolean>()
        {
        @Override protected void actionOnLoopThread()
            {
            this.result = target.isI2cPortActionFlagSet(physicalPort);
            }
        }).doReadOperation();
        }

    @Override public void readI2cCacheFromModule(final int physicalPort)
        {
        (new ThunkForWriting()
        {
        @Override protected void actionOnLoopThread()
            {
            target.readI2cCacheFromModule(physicalPort);
            }
        }).doWriteOperation();
        }

    @Override public void writeI2cCacheToModule(final int physicalPort)
        {
        (new ThunkForWriting()
        {
        @Override protected void actionOnLoopThread()
            {
            target.writeI2cCacheToModule(physicalPort);
            }
        }).doWriteOperation();
        }

    @Override public void writeI2cPortFlagOnlyToModule(final int physicalPort)
        {
        (new ThunkForWriting()
        {
        @Override protected void actionOnLoopThread()
            {
            target.writeI2cPortFlagOnlyToModule(physicalPort);
            }
        }).doWriteOperation();
        }

    @Override public boolean isI2cPortInReadMode(final int physicalPort)
        {
        return (new ThunkForReading<Boolean>()
        {
        @Override protected void actionOnLoopThread()
            {
            this.result = target.isI2cPortInReadMode(physicalPort);
            }
        }).doReadOperation();
        }

    @Override public boolean isI2cPortInWriteMode(final int physicalPort)
        {
        return (new ThunkForReading<Boolean>()
        {
        @Override protected void actionOnLoopThread()
            {
            this.result = target.isI2cPortInWriteMode(physicalPort);
            }
        }).doReadOperation();
        }

    @Override public boolean isI2cPortReady(final int physicalPort)
        {
        return (new ThunkForReading<Boolean>()
        {
        @Override protected void actionOnLoopThread()
            {
            this.result = target.isI2cPortReady(physicalPort);
            }
        }).doReadOperation();
        }

    /**
     * @hide
     */
    @Override public void registerForI2cPortReadyCallback(final I2cController.I2cPortReadyCallback callback, final int physicalPort)
        {
        (new ThunkForWriting()
            {
            @Override protected void actionOnLoopThread()
                {
                target.registerForI2cPortReadyCallback(callback, physicalPort);
                }
            }).doWriteOperation();
        }

    /**
     * @hide
     */
    @Override public void deregisterForPortReadyCallback(final int physicalPort)
        {
        (new ThunkForWriting()
            {
            @Override protected void actionOnLoopThread()
                {
                target.deregisterForPortReadyCallback(physicalPort);
                }
            }).doWriteOperation();
        }

    }
