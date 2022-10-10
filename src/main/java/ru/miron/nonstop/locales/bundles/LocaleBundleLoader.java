package ru.miron.nonstop.locales.bundles;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.Scanner;

public class LocaleBundleLoader {

    public static ListResourceBundle load(String generalPath, Locale localeToGetPostfixFrom) throws FileNotFoundException, IllegalStateException {
        var localePostfix = localeToGetPostfixFrom.getDisplayName();
        return LocaleBundleLoader.load(generalPath, localePostfix);
    }

    public static ListResourceBundle load(String generalPath, String localePostfix) throws FileNotFoundException, IllegalStateException {
        String path = generalPath + "_" + localePostfix + ".properties";
        var bundleFile = new File(path);
        var bundleScanner = new Scanner(bundleFile, "UTF-8");
        var bundleList = new LinkedList<String[]>();
        while (bundleScanner.hasNextLine()) {
            var nextLine = bundleScanner.nextLine();
            nextLine = nextLine.split("\\/\\/")[0];
            if (nextLine.isBlank()) {
                continue;
            }
            var splitted = nextLine.split("=");
            if (splitted.length != 2) {
                throw new IllegalStateException(nextLine);
            }
            bundleList.add(splitted);
        }
        var bundleArray = new String[bundleList.size()][2];
        bundleList.toArray(bundleArray);
        return new ListResourceBundle() {
            @Override
            protected Object[][] getContents() {
                return bundleArray;
            }
        };
    }
}
