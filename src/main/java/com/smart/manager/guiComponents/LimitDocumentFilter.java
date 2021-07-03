package com.smart.manager.guiComponents;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * Creates a {@link DocumentFilter} for text fields. This one limits the amount of characters inside a field
 */
public class LimitDocumentFilter extends DocumentFilter {
    /**
     * The maximum amount of characters in the field
     */
    private final int limit;

    /**
     * The constructor for creating the filter
     * @param limit The maximum amount of characters the text field may contain
     */
    public LimitDocumentFilter(int limit) {
        if (limit <= 0)
            throw new IllegalArgumentException("Limit can not be <= 0");
        this.limit = limit;
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        // Get the current length of the document
        int currentLength = fb.getDocument().getLength();
        // Calculates the int for the number of characters the field is over/under by
        int overLimit = (currentLength + text.length()) - limit - length;
        // If the text is over the limit, the contents will be updated to a value that is not
        if (overLimit > 0)
            text = text.substring(0, text.length() - overLimit);
        if (text.length() > 0)
            super.replace(fb, offset, length, text, attrs);
    }

}
