package org.ekstep.devcon.game;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import org.ekstep.devcon.R;
import org.ekstep.devcon.util.PreferenceUtil;

/**
 * @author vinayagasundar
 */

public class RulesDialogFragment extends DialogFragment {

    public static final String USER_RULES = "rules";

    private Callback mCallback;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_rules_fragment, container, false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setCancelable(false);

        View okButton = view.findViewById(R.id.ok_button);
        View cancelButton = view.findViewById(R.id.cancel_button);

        if (getActivity() instanceof Callback) {
            mCallback = (Callback) getActivity();
        }

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceUtil.getInstance().setBooleanValue(USER_RULES, true);
                if (mCallback != null) {
                    mCallback.onAgree();
                }

                dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceUtil.getInstance().setBooleanValue(USER_RULES, false);
                if (mCallback != null) {
                    mCallback.onDisAgree();
                }
                dismiss();
            }
        });
    }

    public interface Callback {
        void onAgree();

        void onDisAgree();
    }
}
