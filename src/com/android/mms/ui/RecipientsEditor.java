/*
 * Copyright (C) 2008 Esmertec AG.
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.mms.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.provider.Telephony.Mms;
import android.telephony.PhoneNumberUtils;
import android.text.Annotation;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.util.Rfc822Token;
import android.text.util.Rfc822Tokenizer;
import android.util.AttributeSet;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;

import com.android.mms.MmsConfig;
import com.android.mms.data.Contact;
import com.android.mms.data.ContactList;

/**
 * Provide UI for editing the recipients of multi-media messages.
 */
public class RecipientsEditor extends EditText {


	private int mLongPressedPosition = -1;
    private char mLastSeparator = ',';
    private Runnable mOnSelectChipRunnable;
    
    public RecipientsEditor(Context context) {
		super(context);
	}
    public RecipientsEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public void setOnSelectChipRunnable(Runnable onSelectChipRunnable) {
        mOnSelectChipRunnable = onSelectChipRunnable;
    }

    public int getRecipientCount() {
    	List<String> numberList = getNumbers();
        return numberList.size();
    }

    public List<String> getNumbers() {
    	ArrayList<String> numberList = new ArrayList<String>();
    	String numbers = getText().toString();
    	if (!TextUtils.isEmpty(numbers)) {
    		numbers = numbers.replaceAll(",", ";");
    		String[] recipients = numbers.split(";");
    		for (String recipient : recipients) {
    			numberList.add(recipient.trim());
			}
		}
        return numberList;
    }

    public ContactList constructContactsFromInput(boolean blocking) {
        List<String> numbers = new ArrayList<String>();
        ContactList list = new ContactList();
        for (String number : numbers) {
            Contact contact = Contact.get(number, blocking);
            contact.setNumber(number);
            list.add(contact);
        }
        return list;
    }

    private boolean isValidAddress(String number, boolean isMms) {
        if (isMms) {
            return MessageUtils.isValidMmsAddress(number);
        } else {
            // TODO: PhoneNumberUtils.isWellFormedSmsAddress() only check if the number is a valid
            // GSM SMS address. If the address contains a dialable char, it considers it a well
            // formed SMS addr. CDMA doesn't work that way and has a different parser for SMS
            // address (see CdmaSmsAddress.parse(String address)). We should definitely fix this!!!
            return PhoneNumberUtils.isWellFormedSmsAddress(number)
                    || Mms.isEmailAddress(number);
        }
    }

    public boolean hasValidRecipient(boolean isMms) {
    	List<String> numbers = new ArrayList<String>();
        for (String number : numbers) {
            if (isValidAddress(number, isMms))
                return true;
        }
        return false;
    }

    public boolean hasInvalidRecipient(boolean isMms) {
    	List<String> numbers = new ArrayList<String>();
        for (String number : numbers) {
            if (!isValidAddress(number, isMms)) {
                if (MmsConfig.getEmailGateway() == null) {
                    return true;
                } else if (!MessageUtils.isAlias(number)) {
                    return true;
                }
            }
        }
        return false;
    }

    public String formatInvalidNumbers(boolean isMms) {
    	List<String> numbers = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        for (String number : numbers) {
            if (!isValidAddress(number, isMms)) {
                if (sb.length() != 0) {
                    sb.append(", ");
                }
                sb.append(number);
            }
        }
        return sb.toString();
    }

    public boolean containsEmail() {
        return false;
    }

    public static CharSequence contactToToken(Contact c) {
        SpannableString s = new SpannableString(c.getNameAndNumber());
        int len = s.length();

        if (len == 0) {
            return s;
        }

        s.setSpan(new Annotation("number", c.getNumber()), 0, len,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return s;
    }

    public void populate(ContactList list) {
    	
    }

    private static String getNumberAt(Spanned sp, int start, int end, Context context) {
        String number = getFieldAt("number", sp, start, end, context);
        number = PhoneNumberUtils.replaceUnicodeDigits(number);
        if (!TextUtils.isEmpty(number)) {
            int pos = number.indexOf('<');
            if (pos >= 0 && pos < number.indexOf('>')) {
                // The number looks like an Rfc882 address, i.e. <fred flinstone> 891-7823
                Rfc822Token[] tokens = Rfc822Tokenizer.tokenize(number);
                if (tokens.length == 0) {
                    return number;
                }
                return tokens[0].getAddress();
            }
        }
        return number;
    }

    private static int getSpanLength(Spanned sp, int start, int end, Context context) {
        // TODO: there's a situation where the span can lose its annotations:
        //   - add an auto-complete contact
        //   - add another auto-complete contact
        //   - delete that second contact and keep deleting into the first
        //   - we lose the annotation and can no longer get the span.
        // Need to fix this case because it breaks auto-complete contacts with commas in the name.
        Annotation[] a = sp.getSpans(start, end, Annotation.class);
        if (a.length > 0) {
            return sp.getSpanEnd(a[0]);
        }
        return 0;
    }

    private static String getFieldAt(String field, Spanned sp, int start, int end,
            Context context) {
        Annotation[] a = sp.getSpans(start, end, Annotation.class);
        String fieldValue = getAnnotation(a, field);
        if (TextUtils.isEmpty(fieldValue)) {
            fieldValue = TextUtils.substring(sp, start, end);
        }
        return fieldValue;

    }

    private static String getAnnotation(Annotation[] a, String key) {
        for (int i = 0; i < a.length; i++) {
            if (a[i].getKey().equals(key)) {
                return a[i].getValue();
            }
        }

        return "";
    }

    private class RecipientsEditorTokenizer
            implements MultiAutoCompleteTextView.Tokenizer {

        @Override
        public int findTokenStart(CharSequence text, int cursor) {
            int i = cursor;
            char c;

            // If we're sitting at a delimiter, back up so we find the previous token
            if (i > 0 && ((c = text.charAt(i - 1)) == ',' || c == ';')) {
                --i;
            }
            // Now back up until the start or until we find the separator of the previous token
            while (i > 0 && (c = text.charAt(i - 1)) != ',' && c != ';') {
                i--;
            }
            while (i < cursor && text.charAt(i) == ' ') {
                i++;
            }

            return i;
        }

        @Override
        public int findTokenEnd(CharSequence text, int cursor) {
            int i = cursor;
            int len = text.length();
            char c;

            while (i < len) {
                if ((c = text.charAt(i)) == ',' || c == ';') {
                    return i;
                } else {
                    i++;
                }
            }

            return len;
        }

        @Override
        public CharSequence terminateToken(CharSequence text) {
            int i = text.length();

            while (i > 0 && text.charAt(i - 1) == ' ') {
                i--;
            }

            char c;
            if (i > 0 && ((c = text.charAt(i - 1)) == ',' || c == ';')) {
                return text;
            } else {
                // Use the same delimiter the user just typed.
                // This lets them have a mixture of commas and semicolons in their list.
                String separator = mLastSeparator + " ";
                if (text instanceof Spanned) {
                    SpannableString sp = new SpannableString(text + separator);
                    TextUtils.copySpansFrom((Spanned) text, 0, text.length(),
                                            Object.class, sp, 0);
                    return sp;
                } else {
                    return text + separator;
                }
            }
        }
        
//        public List<String> getNumbers() {
//            Spanned sp = RecipientsEditor.this.getText();
//            int len = sp.length();
//            List<String> list = new ArrayList<String>();
//
//            int start = 0;
//            int i = 0;
//            while (i < len + 1) {
//                char c;
//                if ((i == len) || ((c = sp.charAt(i)) == ',') || (c == ';')) {
//                    if (i > start) {
//                        list.add(getNumberAt(sp, start, i, getContext()));
//
//                        // calculate the recipients total length. This is so if the name contains
//                        // commas or semis, we'll skip over the whole name to the next
//                        // recipient, rather than parsing this single name into multiple
//                        // recipients.
//                        int spanLen = getSpanLength(sp, start, i, getContext());
//                        if (spanLen > i) {
//                            i = spanLen;
//                        }
//                    }
//
//                    i++;
//
//                    while ((i < len) && (sp.charAt(i) == ' ')) {
//                        i++;
//                    }
//
//                    start = i;
//                } else {
//                    i++;
//                }
//            }
//
//            return list;
//        }
    }

    static class RecipientContextMenuInfo implements ContextMenuInfo {
        final Contact recipient;

        RecipientContextMenuInfo(Contact r) {
            recipient = r;
        }
    }
}
