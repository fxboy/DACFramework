package icu.weboys.dacf.connect.socket;

import icu.weboys.dacf.core.abs.AbsConnector;
import icu.weboys.dacf.core.annotation.DACFConnector;
import icu.weboys.dacf.core.info.ModuleInfo;

@DACFConnector
public class NettyServerConnector extends AbsConnector<byte[]> {
    @Override
    public void init(ModuleInfo info) {
        super.init(info);
    }
}
