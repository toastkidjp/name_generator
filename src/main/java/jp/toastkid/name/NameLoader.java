package jp.toastkid.name;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reactor.core.publisher.Flux;

/**
 * Read nameInformation from passed JSON.
 *
 * @author Toast kid
 */
public class NameLoader implements Callable<Collection<NameInformation>>{

    /** Logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(NameLoader.class);

    /** Name list. */
    private final Collection<NameInformation> names;

    /** Name nationalities. */
    private final Collection<String> nationalities;

    /** target JSON file. */
    private final File targetFile;

    /**
     *
     * @param file
     */
    public NameLoader(final File file) {
        this.targetFile    = file;
        this.nationalities = Sets.mutable.empty();
        this.names         = Lists.mutable.empty();
    }

    @Override
    public Collection<NameInformation> call() {
        try (final Stream<String> fileReader = Files.lines(targetFile.toPath())) {
            final Flux<NameInformation> cache
                = Flux.<NameInformation>create(emitter -> fileReader.forEach(str -> {
                    try {
                        emitter.next(NameInformation.fromJson(str));
                    } catch (final Exception e) {
                        emitter.error(e);
                    }
                })).cache();
            cache.subscribe(names::add);
            cache.subscribe(info -> nationalities.add(info.getNationality()));
            fileReader.close();
        } catch (final IOException e) {
            LOGGER.error("Caught error.", e);
        }
        return names;
    }

    /**
     * get Nationality set.
     * @return
     */
    public Collection<String> getNationalities() {
        return nationalities;
    }

}
