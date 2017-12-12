package org.ekstep.devcon.game;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.ekstep.devcon.R;
import org.ekstep.devcon.game.models.QuestionModel;

/**
 * @author vinayagasundar
 */

public class QuestionDetailDialogFragment extends DialogFragment {

    private static final String BUNDLE_QUESTION_ID = "questionId";

    private TextView mQuestionText;
    private EditText mAnswerEditText;

    private View mQuestionView;

    private QuestionModel mQuestionModel;

    public static QuestionDetailDialogFragment newInstance(QuestionModel questionModel) {
        QuestionDetailDialogFragment fragment = new QuestionDetailDialogFragment();

        Bundle args = new Bundle();
        args.putParcelable(BUNDLE_QUESTION_ID, questionModel);

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mQuestionModel = getArguments().getParcelable(BUNDLE_QUESTION_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_question_detail, container, false);
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
        getDialog().getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        mQuestionText = view.findViewById(R.id.question_text);
        mQuestionView = view.findViewById(R.id.question_view);
        mAnswerEditText = view.findViewById(R.id.answer_edit_text);

        mAnswerEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    processAnswer();
                    return true;
                }
                return false;
            }
        });

        View submitView = view.findViewById(R.id.submit_answer);
        submitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processAnswer();
            }
        });

        setCancelable(false);

        initUI();
    }

    private void initUI() {
        displayQuestion();
    }

    private void displayQuestion() {
        QuestionModel question = mQuestionModel;
        if (question == null) {
            return;
        }

        mQuestionText.setText(mQuestionModel.getQuestion());


        mQuestionView.setVisibility(View.VISIBLE);
    }

    private void processAnswer() {
        String answer = mAnswerEditText.getText().toString();

        if (!TextUtils.isEmpty(answer)) {
            // Check the answer here
            boolean isCorrect = GameEngine.getEngine().isCorrect(answer);

            if (!isCorrect) {
                mAnswerEditText.setText(null);
                Toast.makeText(getActivity(), "Wrong Answer", Toast.LENGTH_SHORT).show();
            }
        } else {
            mAnswerEditText.setError("Please answer the question");
        }
    }
}
