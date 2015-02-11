package com.noe.badger.bundle;

import java.util.List;
import java.util.Properties;
import javax.inject.Named;

@Named
public class LanguageBundle {

    private List<Properties> internationalizationFiles;

    public void setInternationalizationFiles(List<Properties> internationalizationFiles) {
        this.internationalizationFiles = internationalizationFiles;
    }
}
