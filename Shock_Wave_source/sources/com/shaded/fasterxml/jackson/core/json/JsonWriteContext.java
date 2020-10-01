package com.shaded.fasterxml.jackson.core.json;

import com.shaded.fasterxml.jackson.core.JsonStreamContext;

public class JsonWriteContext extends JsonStreamContext {
    public static final int STATUS_EXPECT_NAME = 5;
    public static final int STATUS_EXPECT_VALUE = 4;
    public static final int STATUS_OK_AFTER_COLON = 2;
    public static final int STATUS_OK_AFTER_COMMA = 1;
    public static final int STATUS_OK_AFTER_SPACE = 3;
    public static final int STATUS_OK_AS_IS = 0;
    protected JsonWriteContext _child = null;
    protected String _currentName;
    protected final JsonWriteContext _parent;

    protected JsonWriteContext(int i, JsonWriteContext jsonWriteContext) {
        this._type = i;
        this._parent = jsonWriteContext;
        this._index = -1;
    }

    public static JsonWriteContext createRootContext() {
        return new JsonWriteContext(0, (JsonWriteContext) null);
    }

    private JsonWriteContext reset(int i) {
        this._type = i;
        this._index = -1;
        this._currentName = null;
        return this;
    }

    public final JsonWriteContext createChildArrayContext() {
        JsonWriteContext jsonWriteContext = this._child;
        if (jsonWriteContext != null) {
            return jsonWriteContext.reset(1);
        }
        JsonWriteContext jsonWriteContext2 = new JsonWriteContext(1, this);
        this._child = jsonWriteContext2;
        return jsonWriteContext2;
    }

    public final JsonWriteContext createChildObjectContext() {
        JsonWriteContext jsonWriteContext = this._child;
        if (jsonWriteContext != null) {
            return jsonWriteContext.reset(2);
        }
        JsonWriteContext jsonWriteContext2 = new JsonWriteContext(2, this);
        this._child = jsonWriteContext2;
        return jsonWriteContext2;
    }

    public final JsonWriteContext getParent() {
        return this._parent;
    }

    public final String getCurrentName() {
        return this._currentName;
    }

    public final int writeFieldName(String str) {
        if (this._type != 2 || this._currentName != null) {
            return 4;
        }
        this._currentName = str;
        return this._index < 0 ? 0 : 1;
    }

    public final int writeValue() {
        if (this._type == 2) {
            if (this._currentName == null) {
                return 5;
            }
            this._currentName = null;
            this._index++;
            return 2;
        } else if (this._type == 1) {
            int i = this._index;
            this._index++;
            if (i >= 0) {
                return 1;
            }
            return 0;
        } else {
            this._index++;
            if (this._index != 0) {
                return 3;
            }
            return 0;
        }
    }

    /* access modifiers changed from: protected */
    public final void appendDesc(StringBuilder sb) {
        if (this._type == 2) {
            sb.append('{');
            if (this._currentName != null) {
                sb.append('\"');
                sb.append(this._currentName);
                sb.append('\"');
            } else {
                sb.append('?');
            }
            sb.append('}');
        } else if (this._type == 1) {
            sb.append('[');
            sb.append(getCurrentIndex());
            sb.append(']');
        } else {
            sb.append("/");
        }
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder(64);
        appendDesc(sb);
        return sb.toString();
    }
}
