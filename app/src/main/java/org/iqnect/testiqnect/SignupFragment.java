package org.iqnect.testiqnect;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import static android.util.Patterns.EMAIL_ADDRESS;

public class SignupFragment extends Fragment {


    private EditText mEmailEdit;
    private EditText mPasswordEdit;

    private TextInputLayout mEmailInput;
    private TextInputLayout mPasswordInput;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_signup, container, false);

        mEmailEdit = (EditText) rootView.findViewById(R.id.edit_email);
        mPasswordEdit = (EditText) rootView.findViewById(R.id.edit_pwd);

        mEmailInput = (TextInputLayout) rootView.findViewById(R.id.input_email);
        mPasswordInput = (TextInputLayout) rootView.findViewById(R.id.input_pwd);


        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private boolean isEmailValid(String email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        } else {
            return EMAIL_ADDRESS.matcher(email).matches();
        }
    }

    public boolean checkSignUpFieldsValidity() {

        boolean isError = false;

        if (!isEmailValid(mEmailEdit.getText().toString())) {
            mEmailInput.setError(getString(R.string.invalid_email));
            isError = true;
        } else {
            mEmailInput.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(mPasswordEdit.getText())) {
            mPasswordInput.setError(getString(R.string.invalid_pwd));
            isError = true;
        } else {
            mPasswordInput.setErrorEnabled(false);
        }

        return !isError;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}