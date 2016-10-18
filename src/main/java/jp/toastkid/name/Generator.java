package jp.toastkid.name;

import java.io.File;
import java.util.Collection;

import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 * Name Generator.
 *
 * @author Toast kid
 */
public class Generator {

    /** Logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    /** dir of assets. */
    private static final String ASSETS_DIR = "assets";

    /** path/to/firstname/file. */
    private static final String FIRST_NAME_FILE
        = ASSETS_DIR + "/resources/NameMaker/first.txt";

    /** path/to/familyname/file. */
    private static final String FAMILY_NAME_FILE
        = ASSETS_DIR + "/resources/NameMaker/family.txt";

    /** file から読み込んだ国籍を格納する. */
    private volatile MutableList<String> nationalities;

    /** file から読み込んだファーストネームを格納する. */
    private MutableList<NameInformation> firstNames;

    /** file から読み込んだファミリーネームを格納する. */
    private MutableList<NameInformation> familyNames;

    /**
     * Construct object.
     */
    public Generator() {
        nationalities = Lists.mutable.empty();

        final Flux<String> first = Flux.<String>create(emitter -> {
            final long start = System.currentTimeMillis();
            final NameLoader loader = new NameLoader(new File(FIRST_NAME_FILE));
            firstNames = Lists.mutable.ofAll(loader.call())
                    .sortThis().distinct();
            loader.getNationalities().forEach(emitter::next);
            emitter.complete();
            LOGGER.info(Thread.currentThread().getName() + " Ended load {} first names. {}[ms]",
                    firstNames.size(), System.currentTimeMillis() - start);
        }).publishOn(Schedulers.elastic());

        final Flux<String> family = Flux.<String>create(emitter -> {
            final long start = System.currentTimeMillis();
            final NameLoader loader = new NameLoader(new File(FAMILY_NAME_FILE));
            familyNames = Lists.mutable.ofAll(loader.call())
                    .sortThis().distinct();
            loader.getNationalities().forEach(emitter::next);
            emitter.complete();
            LOGGER.info(Thread.currentThread().getName() + " Ended load {} family names. {}[ms]",
                    familyNames.size(), System.currentTimeMillis() - start);
        }).publishOn(Schedulers.elastic());

        Flux.merge(first, family).subscribe(nationalities::add);
    }

    /**
     * 名前を生成する
     * @param nationality 国籍は？
     * @param neededValue いくつ生成するか？
     */
    public String generate(final String nationality, final int neededValue) {

        if (nationality == null) {
            return "";
        }

        int x = 0;
        int y = 0;
        NameInformation firstName;
        NameInformation secondName;

        final String lineSeparator = System.lineSeparator();
        final StringBuilder names = new StringBuilder();
        if ("指定しない".equals(nationality) ) {
            for (int i = 0; i < neededValue; i++) {
                x = (int)(Math.random() * firstNames.size());
                y = (int)(Math.random() * familyNames.size());
                firstName  = firstNames.get(x);
                secondName = familyNames.get(y);
                names.append(firstName.getSeibetsu()).append(" , ").append(firstName.getName())
                     .append("・").append(secondName.getName()).append(" , ")
                     .append(firstName.getSpelling()).append(" ").append(secondName.getSpelling())
                     .append(lineSeparator);
            }
        } else {
            for (int i = 0; i < neededValue; i++) {
                x = (int)(Math.random() * firstNames.size());
                y = (int)(Math.random() * familyNames.size());
                firstName  = firstNames.get(x);
                secondName = familyNames.get(y);
                while ( !nationality.equals(firstName.getNationality()) ) {
                    x = (int)(Math.random() * firstNames.size());
                    firstName  = firstNames.get(x);
                }
                while ( !nationality.equals(secondName.getNationality()) ) {
                    x = (int)(Math.random()*familyNames.size());
                    secondName  = familyNames.get(x);
                }
                names.append(firstName.getSeibetsu()).append(" , ").append(firstName.getName())
                     .append("・").append(secondName.getName()).append(" , ")
                     .append(firstName.getSpelling()).append(" ").append(secondName.getSpelling())
                     .append(lineSeparator);
            }
        }
        return names.toString();
    }

    /**
     * 国籍一覧を返す.
     */
    public Collection<String> getNationalities() {
        return nationalities.distinct().reject(str -> str == null).sortThis();
    }

}
