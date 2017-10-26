package node.com.enjoydanang.ui.fragment.personal;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import node.com.enjoydanang.MvpFragmentWithToolbar;
import node.com.enjoydanang.R;
import rx.Subscription;

/**
 * Created by chientruong on 1/5/17.
 */

public class PersonalFragment extends MvpFragmentWithToolbar<PersonalPresenter> implements PersonalView {


    @BindView(R.id.txt_username)
    TextInputLayout input_username;
    @BindView(R.id.txt_phone)
    TextInputLayout input_phone;
    @BindView(R.id.txt_email)
    TextInputLayout input_email;
    @BindView(R.id.txt_fullname)
    TextInputLayout input_fullname;

    @BindView(R.id.edt_username)
    EditText edtUserName;
    @BindView(R.id.edt_email)
    EditText edtEmail;
    @BindView(R.id.edt_fullname)
    EditText edtFullName;
    @BindView(R.id.edt_phone)
    EditText edtPhone;

    @Override
    protected void init(View view) {
        edtUserName.addTextChangedListener(new MyTextWatcher(edtUserName));
        edtEmail.addTextChangedListener(new MyTextWatcher(edtEmail));
    }

    @Override
    protected void setEvent(View view) {

    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_personal;
    }

    @Override
    public void bindView(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected PersonalPresenter createPresenter() {
        return new PersonalPresenter(this);
    }

    @Override
    public void showToast(String desc) {

    }

    @Override
    public void unKnownError() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void setupActionBar() {


    }
    @OnClick(R.id.btn_save)
    public void submit(View view) {

    }


    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.edt_username:
                    validateName();
                    break;
                case R.id.edt_email:
                    validateEmail();
                    break;

            }
        }
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            mMainActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    private boolean validateName() {
        if (edtUserName.getText().toString().trim().isEmpty()) {
            input_username.setError("Invalid");
            requestFocus(edtUserName);
            return false;
        } else {
            input_username.setErrorEnabled(false);
        }
        return true;
    }
    private boolean validateEmail() {
        String email = edtEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            input_email.setError(getString(R.string.err_msg_email));
            requestFocus(edtEmail);
            return false;
        } else {
            input_email.setErrorEnabled(false);
        }

        return true;
    }
    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}
