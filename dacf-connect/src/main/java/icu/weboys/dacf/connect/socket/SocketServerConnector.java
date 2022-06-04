package icu.weboys.dacf.connect.socket;

import icu.weboys.dacf.core.abs.AbsConnector;
import icu.weboys.dacf.core.annotation.DACFConnector;

@DACFConnector
public class SocketServerConnector extends AbsConnector<byte[]> {
    @Override
    public void init(String name) {
        super.init(name);
    }
}
