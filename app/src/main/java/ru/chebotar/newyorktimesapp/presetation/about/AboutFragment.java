package ru.chebotar.newyorktimesapp.presetation.about;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import ru.chebotar.newyorktimesapp.R;
import ru.chebotar.newyorktimesapp.presetation.base.BaseFragment;

public class AboutFragment extends BaseFragment {

    private Button sendMessageButton;
    private EditText editText;
    private ImageView facebookLogo;
    private ImageView instagramLogo;
    private ImageView vkLogo;

    @Override
    protected int setLayoutRes() {
        return R.layout.fragment_about;
    }

    public static Fragment getNewInstance(Bundle data) {
        AboutFragment fragment = new AboutFragment();
        fragment.setArguments(data);
        return fragment;
    }

    public static Bundle getBundle() {
        Bundle arguments = new Bundle();
        return arguments;
    }

    @Override
    protected void onPostCreateView() {
        sendMessageButton = rootView.findViewById(R.id.send_message);
        editText = rootView.findViewById(R.id.message);
        facebookLogo = rootView.findViewById(R.id.facebook_logo);
        instagramLogo = rootView.findViewById(R.id.instagram_logo);
        vkLogo = rootView.findViewById(R.id.vk_logo);
        vkLogo.setOnClickListener(view -> openWeb(getString(R.string.vk_url)));
        instagramLogo.setOnClickListener(view -> openWeb(getString(R.string.insta_url)));
        facebookLogo.setOnClickListener(view -> openWeb(getString(R.string.fb_url)));
        sendMessageButton.setOnClickListener(view -> sendMessage());
        editText.setOnEditorActionListener((view, actionId, event) -> {
            if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER
                    || actionId == EditorInfo.IME_ACTION_DONE) {
                sendMessage();
            }
            return false;
        });
    }

    private void openWeb(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            showToast(R.string.no_email_app);
        }
    }

    private void sendMessage() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:some@gmail.com" +
                "?subject=" + Uri.encode("Awesome Subject") +
                "&body=" + Uri.encode(editText.getText().toString().trim())));
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(Intent.createChooser(intent, "Send Email"));
        } else {
            showToast(R.string.no_email_app);
        }
    }

    @Override
    protected void configureToolbar(@NonNull Toolbar toolbar) {
        super.configureToolbar(toolbar);
        toolbar.setTitle(R.string.name);
    }

    @Override
    public boolean onBackPressed() {
        return true;
    }
}
