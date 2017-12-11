package org.ekstep.devcon;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.ekstep.devcon.model.Option;
import org.ekstep.devcon.model.QuestionModel;
import org.ekstep.devcon.util.TreasureHuntUtil;

import java.util.List;

/**
 * @author vinayagasundar
 */

public class QuestionDetailDialogFragment extends DialogFragment
        implements RadioGroup.OnCheckedChangeListener {

    private static final String BUNDLE_QUESTION_ID = "questionId";

    private String mQuestionId;

    private ImageView mQRImage;

    private TextView mQuestionText;
    private RadioButton mOptionOne;
    private RadioButton mOptionTwo;
    private RadioButton mOptionThree;
    private RadioButton mOptionFour;

    private View mLoaderView;
    private View mQuestionView;
    private View mHintView;

    private QuestionModel mQuestionModel;

    public static QuestionDetailDialogFragment newInstance(String questionId) {
        QuestionDetailDialogFragment fragment = new QuestionDetailDialogFragment();

        Bundle args = new Bundle();
        args.putString(BUNDLE_QUESTION_ID, questionId);

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mQuestionId = getArguments().getString(BUNDLE_QUESTION_ID, null);
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
        //Set the dialog to immersive
        dialog.getWindow().getDecorView().setSystemUiVisibility(getActivity().getWindow().getDecorView().getSystemUiVisibility());
        return dialog;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);

        mQRImage = view.findViewById(R.id.qr_code_img);

        mQuestionText = view.findViewById(R.id.question_text);
        mOptionOne = view.findViewById(R.id.option_one);
        mOptionTwo = view.findViewById(R.id.option_two);
        mOptionThree = view.findViewById(R.id.option_three);
        mOptionFour = view.findViewById(R.id.option_four);

        mQuestionView = view.findViewById(R.id.question_view);
        mLoaderView = view.findViewById(R.id.loader_view);
        mHintView = view.findViewById(R.id.hint_view);

        View mScanButton = view.findViewById(R.id.scan_qr_code);
        mScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        RadioGroup optionRadioGroup = view.findViewById(R.id.option_radio_group);
        optionRadioGroup.setOnCheckedChangeListener(this);

        initUI();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int optionId = -1;

        switch (checkedId) {
            case R.id.option_one:
                optionId = 1;
                break;

            case R.id.option_two:
                optionId = 2;
                break;

            case R.id.option_three:
                optionId = 3;
                break;

            case R.id.option_four:
                optionId = 4;
                break;
        }

        if (mQuestionModel.getAnswer() == optionId) {
            showHint();
        } else {
            Toast.makeText(getActivity(), "Wrong Answer", Toast.LENGTH_SHORT).show();
            group.clearCheck();
        }
    }

    private void showHint() {
        final TextView hintText = mHintView.findViewById(R.id.hint_text);

        mHintView.setVisibility(View.VISIBLE);

        mQuestionView.animate()
                .setDuration(250)
                .alpha(0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mQuestionView.setVisibility(View.GONE);

                        hintText.setText(mQuestionModel.getHint());
                    }
                });
    }

    private void initUI() {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(mQuestionId, BarcodeFormat.QR_CODE,
                    200,200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            mQRImage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        displayQuestion();
    }

    private void displayQuestion() {
        mQuestionModel = TreasureHuntUtil.getQuestion(Integer.parseInt(mQuestionId));

        if (mQuestionModel == null) {
            return;
        }

        mQuestionText.setText(mQuestionModel.getQuestion());
        List<Option> options = mQuestionModel.getOptions();

        mOptionOne.setText(options.get(0).getOptionText());
        mOptionTwo.setText(options.get(1).getOptionText());
        mOptionThree.setText(options.get(2).getOptionText());
        mOptionFour.setText(options.get(3).getOptionText());
        mQuestionView.setVisibility(View.VISIBLE);

        mLoaderView.animate()
                .alpha(0f)
                .setDuration(250)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mLoaderView.setVisibility(View.GONE);

                    }
                })
                .start();
    }
}
