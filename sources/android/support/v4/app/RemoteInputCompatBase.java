package android.support.v4.app;

import android.os.Bundle;
import java.util.Set;

@Deprecated
class RemoteInputCompatBase {

    @Deprecated
    public static abstract class RemoteInput {

        @Deprecated
        public interface Factory {
            RemoteInput build(String str, CharSequence charSequence, CharSequence[] charSequenceArr, boolean z, Bundle bundle, Set<String> set);

            RemoteInput[] newArray(int i);
        }

        @Deprecated
        public abstract boolean getAllowFreeFormInput();

        @Deprecated
        public abstract Set<String> getAllowedDataTypes();

        @Deprecated
        public abstract CharSequence[] getChoices();

        @Deprecated
        public abstract Bundle getExtras();

        @Deprecated
        public abstract CharSequence getLabel();

        @Deprecated
        public abstract String getResultKey();
    }

    RemoteInputCompatBase() {
    }
}
