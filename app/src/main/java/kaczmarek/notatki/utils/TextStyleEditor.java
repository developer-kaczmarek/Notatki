package kaczmarek.notatki.utils;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.QuoteSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.widget.EditText;


public class TextStyleEditor {
    private EditText mEditText;
    private SpannableStringBuilder mSpannableText;
    private int startStyle, endStyle, startSelect, endSelect, colorAccent, beginParagraph;
    private static final String HTML_BULLETPOINT = "\u2022 ";
    UnderlineSpan[] underlineArray;
    StyleSpan[] textStyleArray;
    StrikethroughSpan[] strikethroughArray;
    QuoteSpan[] quoteArray;
    Object[] spansToRemove;
    //============= C O N S T R U C T O R =============
    public TextStyleEditor(EditText mEditText, int colorAccent) {
        this.mEditText = mEditText;
        this.mSpannableText = new SpannableStringBuilder(mEditText.getText());
        this.colorAccent = colorAccent;
    }
    //============= C O N S T R U C T O R =============

    //============= U N D E R L I N E  S T Y L E =============
    public void setUnderlineStyle(){
        mSpannableText.setSpan(new UnderlineSpan(), startSelect, endSelect, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public void removeUnderlineStyle(){
        for(UnderlineSpan underline : underlineArray){
            startStyle = mSpannableText.getSpanStart(underline);
            endStyle = mSpannableText.getSpanEnd(underline);

            if (startStyle == startSelect && endStyle == endSelect) {
                mSpannableText.removeSpan(underline);
            } else if (startStyle < startSelect && endStyle > endSelect) {
                mSpannableText.removeSpan(underline);
                mSpannableText.setSpan(new UnderlineSpan(), startStyle, startSelect, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                mSpannableText.setSpan(new UnderlineSpan(), endSelect, endStyle, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (startStyle < startSelect && endStyle >= startSelect) {
                mSpannableText.removeSpan(underline);
                mSpannableText.setSpan(new UnderlineSpan(), startStyle, startSelect, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (endSelect < endStyle && endSelect >= startStyle) {
                mSpannableText.removeSpan(underline);
                mSpannableText.setSpan(new UnderlineSpan(), endSelect, endStyle, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    public void initUnderlineStyleArray(int startSelect,int endSelect){
        this.startSelect = startSelect;
        this.endSelect = endSelect;
        underlineArray = mSpannableText.getSpans(this.startSelect,  this.endSelect, UnderlineSpan.class);
    }

    public boolean isUnderlineStyle(){
        boolean stateStyle;
        if(underlineArray.length == 0)
            stateStyle = false;
        else
            stateStyle = true;
        return stateStyle;
    }
    //============= U N D E R L I N E  S T Y L E =============

    //================ Q U O T E  S T Y L E ================
    public void setOuoteStyle() {
        int beginParagraph = TextUtils.lastIndexOf(mSpannableText, '\n', startSelect);
        int endParagraph = TextUtils.indexOf(mSpannableText, '\n', startSelect);

        if (beginParagraph != -1 && endParagraph != -1) {
            mSpannableText.setSpan(new QuoteSpan(colorAccent), beginParagraph + 1, endParagraph, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        } else if (beginParagraph == -1 && endParagraph != -1) {
            mSpannableText.setSpan(new QuoteSpan(colorAccent), 0, endParagraph, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        } else if (beginParagraph != -1 && endParagraph == -1) {
            mSpannableText.setSpan(new QuoteSpan(colorAccent), beginParagraph + 1, mEditText.getText().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        } else if (beginParagraph == -1 && endParagraph == -1) {
            mSpannableText.setSpan(new QuoteSpan(colorAccent), 0, mEditText.getText().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        }
    }

    public void removeOuoteStyle(){
        for(QuoteSpan quote : quoteArray){
            mSpannableText.removeSpan(quote);
        }
    }

    public void initOuoteStyleArray(int startSelect,int endSelect){
        this.startSelect = startSelect;
        this.endSelect = endSelect;
        quoteArray = mSpannableText.getSpans( this.startSelect, this.endSelect, QuoteSpan.class);
    }

    public boolean isOuoteStyle(){
        boolean stateStyle;
        if(quoteArray.length == 0)
            stateStyle = false;
        else
            stateStyle = true;
        return stateStyle;
    }
    //================ Q U O T E  S T Y L E ================

    //========= S T R I K E T H R O U G H  S T Y L E =========
    public void setStrikethroughStyle(){
        mSpannableText.setSpan(new StrikethroughSpan(),  startSelect, endSelect, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public void removeStrikethroughStyle(){
        for(StrikethroughSpan strikethrough : strikethroughArray){
            startStyle = mSpannableText.getSpanStart(strikethrough);
            endStyle = mSpannableText.getSpanEnd(strikethrough);

            if (startStyle == startSelect && endStyle == endSelect) {
                mSpannableText.removeSpan(strikethrough);
            } else if (startStyle < startSelect && endStyle > endSelect) {
                mSpannableText.removeSpan(strikethrough);
                mSpannableText.setSpan(new StrikethroughSpan(), startStyle, startSelect, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                mSpannableText.setSpan(new StrikethroughSpan(), endSelect, endStyle, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (startStyle < startSelect && endStyle >= startSelect) {
                mSpannableText.removeSpan(strikethrough);
                mSpannableText.setSpan(new StrikethroughSpan(), startStyle, startSelect, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (endSelect < endStyle && endSelect >= startStyle) {
                mSpannableText.removeSpan(strikethrough);
                mSpannableText.setSpan(new StrikethroughSpan(), endSelect, endStyle, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    public void initStrikethroughStyleArray(int startSelect,int endSelect){
        this.startSelect = startSelect;
        this.endSelect = endSelect;
        strikethroughArray = mSpannableText.getSpans( this.startSelect, this.endSelect, StrikethroughSpan.class);
    }

    public boolean isStrikethroughStyle(){
        boolean stateStyle;
        if(strikethroughArray.length == 0)
            stateStyle = false;
        else
            stateStyle = true;
        return stateStyle;
    }
    //========= S T R I K E T H R O U G H  S T Y L E =========

    //================ B U L L E T  S T Y L E ================
    public void setBulletStyle() {
        mSpannableText.insert(beginParagraph+1,HTML_BULLETPOINT);
    }

    public void removeBulletStyle(){
        mSpannableText.delete(beginParagraph+1,beginParagraph+3);
    }

    public void initBulletStyleArray(int startSelect,int endSelect){
        this.startSelect = startSelect;
        this.endSelect = endSelect;
        this.beginParagraph = TextUtils.lastIndexOf(mSpannableText, '\n', startSelect);
    }

    public boolean isBulletStyle(){
        boolean stateStyle;
        if(!(mSpannableText.charAt(beginParagraph+1)=='\u2022')){
            stateStyle = false;
        } else {
            stateStyle = true;
        }
        return stateStyle;
    }
    //================ B U L L E T  S T Y L E ================

    //======= B O L D / I T A L I C / B O L D I T A L I C  S T Y L E =======
    public void setBoldStyle(){
        mSpannableText.setSpan(new StyleSpan(Typeface.BOLD),startSelect, endSelect, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public void setItalicStyle(){
        mSpannableText.setSpan(new StyleSpan(Typeface.ITALIC),startSelect, endSelect, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public void editBoldStyle(){
        for (StyleSpan style : textStyleArray) {
            startStyle = mSpannableText.getSpanStart(style);
            endStyle = mSpannableText.getSpanEnd(style);
            if(style.getStyle() == Typeface.BOLD){
                mSpannableText.removeSpan(style);
                if (startStyle < startSelect && endStyle > endSelect) {
                    mSpannableText.setSpan(new StyleSpan(Typeface.BOLD), startStyle, startSelect, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mSpannableText.setSpan(new StyleSpan(Typeface.BOLD), endSelect, endStyle, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if (startStyle < startSelect && endStyle >= startSelect) {
                    mSpannableText.setSpan(new StyleSpan(Typeface.BOLD), startStyle, startSelect, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if (endSelect < endStyle && endSelect >= startStyle) {
                    mSpannableText.setSpan(new StyleSpan(Typeface.BOLD), endSelect, endStyle, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            } else if(style.getStyle() == Typeface.ITALIC){
                mSpannableText.removeSpan(style);
                if (startStyle == startSelect && endStyle == endSelect) {
                    mSpannableText.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), startSelect, endSelect, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if (startStyle < startSelect && endStyle > endSelect) {
                    mSpannableText.setSpan(new StyleSpan(Typeface.ITALIC), startStyle, startSelect, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mSpannableText.setSpan(new StyleSpan(Typeface.ITALIC), endSelect, endStyle, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mSpannableText.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), startSelect, endSelect, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if (startStyle < startSelect && endStyle >= startSelect) {
                    mSpannableText.setSpan(new StyleSpan(Typeface.ITALIC), startStyle, startSelect, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mSpannableText.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), startSelect, endSelect, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if (endSelect < endStyle && endSelect >= startStyle) {
                    mSpannableText.setSpan(new StyleSpan(Typeface.ITALIC), endSelect, endStyle, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mSpannableText.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), startSelect, endSelect, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            } else if (style.getStyle() == Typeface.BOLD_ITALIC){
                mSpannableText.removeSpan(style);
                if (startStyle == startSelect && endStyle == endSelect) {
                    mSpannableText.setSpan(new StyleSpan(Typeface.ITALIC), startSelect, endSelect, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if (startStyle < startSelect && endStyle > endSelect) {
                    mSpannableText.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), startStyle, startSelect, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mSpannableText.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), endSelect, endStyle, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mSpannableText.setSpan(new StyleSpan(Typeface.ITALIC), startSelect, endSelect, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if (startStyle < startSelect && endStyle >= startSelect) {
                    mSpannableText.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), startStyle, startSelect, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mSpannableText.setSpan(new StyleSpan(Typeface.ITALIC), startSelect, endSelect, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if (endSelect < endStyle && endSelect >= startStyle) {
                    mSpannableText.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), endSelect, endStyle, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mSpannableText.setSpan(new StyleSpan(Typeface.ITALIC), startSelect, endSelect, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
    }

    public void editItalicStyle(){
        for (StyleSpan style : textStyleArray) {
            startStyle = mSpannableText.getSpanStart(style);
            endStyle = mSpannableText.getSpanEnd(style);
            if(style.getStyle() == Typeface.ITALIC){
                mSpannableText.removeSpan(style);
                if (startStyle < startSelect && endStyle > endSelect) {
                    mSpannableText.setSpan(new StyleSpan(Typeface.ITALIC), startStyle, startSelect, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mSpannableText.setSpan(new StyleSpan(Typeface.ITALIC), endSelect, endStyle, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if (startStyle < startSelect && endStyle >= startSelect) {
                    mSpannableText.setSpan(new StyleSpan(Typeface.ITALIC), startStyle, startSelect, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if (endSelect < endStyle && endSelect >= startStyle) {
                    mSpannableText.setSpan(new StyleSpan(Typeface.ITALIC), endSelect, endStyle, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            } else if(style.getStyle() == Typeface.BOLD){
                mSpannableText.removeSpan(style);
                if (startStyle == startSelect && endStyle == endSelect) {
                    mSpannableText.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), startSelect, endSelect, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if (startStyle < startSelect && endStyle > endSelect) {
                    mSpannableText.setSpan(new StyleSpan(Typeface.BOLD), startStyle, startSelect, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mSpannableText.setSpan(new StyleSpan(Typeface.BOLD), endSelect, endStyle, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mSpannableText.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), startSelect, endSelect, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if (startStyle < startSelect && endStyle >= startSelect) {
                    mSpannableText.setSpan(new StyleSpan(Typeface.BOLD), startStyle, startSelect, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mSpannableText.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), startSelect, endSelect, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if (endSelect < endStyle && endSelect >= startStyle) {
                    mSpannableText.setSpan(new StyleSpan(Typeface.BOLD), endSelect, endStyle, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mSpannableText.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), startSelect, endSelect, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            } else if (style.getStyle() == Typeface.BOLD_ITALIC){
                mSpannableText.removeSpan(style);
                if (startStyle == startSelect && endStyle == endSelect) {
                    mSpannableText.setSpan(new StyleSpan(Typeface.BOLD), startSelect, endSelect, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if (startStyle < startSelect && endStyle > endSelect) {
                    mSpannableText.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), startStyle, startSelect, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mSpannableText.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), endSelect, endStyle, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mSpannableText.setSpan(new StyleSpan(Typeface.BOLD), startSelect, endSelect, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if (startStyle < startSelect && endStyle >= startSelect) {
                    mSpannableText.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), startStyle, startSelect, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mSpannableText.setSpan(new StyleSpan(Typeface.BOLD), startSelect, endSelect, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if (endSelect < endStyle && endSelect >= startStyle) {
                    mSpannableText.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), endSelect, endStyle, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mSpannableText.setSpan(new StyleSpan(Typeface.BOLD), startSelect, endSelect, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
    }

    public void initTextStyleArray(int startSelect,int endSelect){
        this.startSelect = startSelect;
        this.endSelect = endSelect;
        textStyleArray = mSpannableText.getSpans(this.startSelect, this.endSelect, StyleSpan.class);
    }

    public boolean isTextStyle(){
        boolean stateStyle;
        if(textStyleArray.length == 0)
            stateStyle = false;
        else
            stateStyle = true;
        return stateStyle;
    }
    //======= B O L D / I T A L I C / B O L D I T A L I C  S T Y L E =======

    //======= R E T U R N  T E X T  S T Y L I E S =======
    public Spannable returnSpannableText(){
        return mSpannableText;
    }
    //======= R E T U R N  T E X T  S T Y L I E S =======

    //======= R E M O V E  A L L  S T Y L I E S =======
    public void clearAllStylies(){
        spansToRemove = mSpannableText.getSpans(0, mEditText.length(), Object.class);
        for(Object span: spansToRemove) {
            mSpannableText.removeSpan(span);
        }
        String myString = mSpannableText.toString().replaceAll(HTML_BULLETPOINT, "");
        mSpannableText = new SpannableStringBuilder(myString);
    }
    //======= R E M O V E  A L L  S T Y L I E S =======
}
