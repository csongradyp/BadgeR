package com.noe.badger.bundle;

import java.util.List;
import java.util.ResourceBundle;
import javax.inject.Named;

@Named
public class LanguageBundle {

    private List<ResourceBundle> internationalizationFiles;

    public void setInternationalizationFiles(List<ResourceBundle> internationalizationFiles) {
        this.internationalizationFiles = internationalizationFiles;
    }

//    public ResourceBundle getLanguageResource()
}
