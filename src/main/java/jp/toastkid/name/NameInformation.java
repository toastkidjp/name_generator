package jp.toastkid.name;

import java.util.Optional;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

/**
 * Name information.
 *
 * @author Toast kid
 */
public final class NameInformation implements Comparable<NameInformation> {

    /** empty object. */
    private static final NameInformation PRESENT = new NameInformation.Builder().build();

    /** name. */
    private final String name;

    /** name's spelling. */
    private final String spelling;

    /** name's gender. */
    private final String seibetsu;

    /** name's nationality. */
    private final String nationality;

    /**
     * Builder.
     * @author Toast kid
     *
     */
    public static class Builder {

        private String name;

        private String spelling;

        private String seibetsu;

        private String nationality;
        /**
         *
         * @param name
         */
        public Builder setName(final String name){
            this.name = name;
            return this;
        }
        /**
         *
         * @param spelling
         */
        public Builder setSpelling(final String spelling){
            this.spelling = spelling;
            return this;
        }
        /**
         *
         * @param seibetsu
         */
        public Builder setSeibetsu(final String seibetsu){
            this.seibetsu = seibetsu;
            return this;
        }
        /**
         *
         * @param nationality
         */
        public Builder setNationality(final String nationality){
            this.nationality = nationality;
            return this;
        }

        public NameInformation build() {
            return new NameInformation(this);
        }
    }

    /**
     * can call only class internal.
     * @param builder
     */
    private NameInformation(final Builder builder) {
        this.name        = builder.name;
        this.nationality = builder.nationality;
        this.seibetsu    = builder.seibetsu;
        this.spelling    = builder.spelling;
    }

    @Override
    public boolean equals(final Object pNameInformation){
        return EqualsBuilder.reflectionEquals(this, pNameInformation, true);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, true);
    }

    @Override
    public int compareTo(final NameInformation obj1) {
        final String nationality1 = obj1.getNationality();
        int ret = 0;
        if ((ret = this.nationality.compareTo(nationality1)) == 0) {
            final String spelling0 = this.getSpelling();
            final String spelling1 = obj1.getSpelling();
            ret = spelling0.compareTo(spelling1);
            if ((ret = spelling0.compareTo(spelling1)) == 0) {
                final String seibetsu0 = this.getSeibetsu();
                final String seibetsu1 = ( obj1).getSeibetsu();
                ret = seibetsu0.compareTo(seibetsu1);
            }
        }
        return ret;
    }

    /**
     *
     * @return
     */
    public String getName(){
         return this.name;
    }
    /**
     *
     * @return
     */
    public String getSpelling(){
         return this.spelling;
    }

    /**
     *
     * @return
     */
    public String getSeibetsu(){
         return this.seibetsu;
    }

    /**
     *
     * @return
     */
    public String getNationality(){
         return this.nationality;
    }

    /**
     * Return empty object.
     * @return
     */
    public static NameInformation empty() {
        return PRESENT;
    }

    /**
     * Make object from JSON string.
     * @param str JSON string
     * @return {@link NameInformation} object
     */
    public static NameInformation fromJson(final String str) {
        final JsonObject json = Json.parse(str).asObject();
        final Builder builder = new NameInformation.Builder();
        builder.setName(json.getString("name", ""))
               .setSpelling(json.getString("spelling", ""))
               .setNationality(json.getString("nationality", ""));
        Optional.ofNullable(json.get("seibetsu"))
                .ifPresent(j -> builder.setSeibetsu(j.asString()));
        return builder.build();
    }
}
