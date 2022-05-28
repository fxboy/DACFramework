package icu.weboys.dacf.connect.socket;

import icu.weboys.dacf.abs.AbsConnector;
import icu.weboys.dacf.annotation.DACFConnector;
import icu.weboys.dacf.info.ModuleInfo;

@DACFConnector
public class NettyServerConnector extends AbsConnector<byte[]> {
    @Override
    public void init(ModuleInfo info) {
        super.init(info);
    }
}
