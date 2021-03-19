package com.visitscotland.brxm.factory;

import com.visitscotland.brxm.hippobeans.LongCopy;
import com.visitscotland.brxm.model.LongCopyModule;
import org.springframework.stereotype.Component;

@Component
public class LongCopyFactory {

    public LongCopyModule getModule(LongCopy doc){
        LongCopyModule module = new LongCopyModule();
        module.setCopy(doc.getCopy());
        return module;
    }
}
