package com.ecommerce.util;

import org.springframework.stereotype.Component;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Utility class for generating URL-friendly slugs
 * 
 * @author E-Commerce Team
 */
@Component
public class SlugGenerator {

    private static final Pattern NON_LATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");
    private static final Pattern EDGES_DASHES = Pattern.compile("(^-|-$)");

    /**
     * Generate a URL-friendly slug from a string
     * 
     * @param input input string
     * @return slug
     */
    public String generateSlug(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "";
        }

        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        String slug = normalized.toLowerCase(Locale.ENGLISH);
        slug = WHITESPACE.matcher(slug).replaceAll("-");
        slug = NON_LATIN.matcher(slug).replaceAll("");
        slug = EDGES_DASHES.matcher(slug).replaceAll("");
        
        return slug;
    }

    /**
     * Generate unique slug with suffix
     * 
     * @param baseSlug base slug
     * @param suffix suffix number
     * @return unique slug
     */
    public String generateUniqueSlug(String baseSlug, int suffix) {
        return baseSlug + "-" + suffix;
    }
}
