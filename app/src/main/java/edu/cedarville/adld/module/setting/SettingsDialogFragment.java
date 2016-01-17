package edu.cedarville.adld.module.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import edu.cedarville.adld.R;
import edu.cedarville.adld.common.manager.SharedPreferencesManager;
import edu.cedarville.adld.common.otto.BusManager;

public class SettingsDialogFragment extends AppCompatDialogFragment {

    @Bind(R.id.radio_group)
    RadioGroup group;

    @Bind(R.id.checkbox_hex)
    AppCompatCheckBox hexCheckBox;

    private SharedPreferencesManager manager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_settings_fragment, container, false);
        ButterKnife.bind(this, view);

        getDialog().setTitle(R.string.running_average);
        this.manager = new SharedPreferencesManager(getActivity());

        this.setSelectedAverage();
        this.setHexChecked();
        this.setCheckListener();

        return view;
    }

    @OnClick(R.id.touch_done)
    void onDoneClicked() {
        getDialog().dismiss();
    }

    @OnCheckedChanged(R.id.checkbox_hex)
    void onCheckHexChanged(boolean checked) {
        manager.setDisplayHex(checked);
        BusManager.getInstance().postDisplayHexChangeEvent();
    }

    private void setSelectedAverage() {
        int avg = manager.getRunningAverage();
        switch (avg) {
            case 1:
                group.check(R.id.radio_raw);
                break;
            case 5:
                group.check(R.id.radio_five);
                break;
            case 10:
                group.check(R.id.radio_ten);
                break;
            case 100:
                group.check(R.id.radio_hundred);
                break;
            default:
                break;
        }
    }

    private void setHexChecked() {
        boolean checked = manager.getDisplayHex();
        this.hexCheckBox.setChecked(checked);
    }

    private void setCheckListener() {
        this.group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton checkedRadioButton = (RadioButton) radioGroup.findViewById(checkedId);

                if (checkedRadioButton.isChecked()) {
                    switch (checkedId) {
                        case R.id.radio_raw:
                            manager.setRunningAverage(1);
                            break;

                        case R.id.radio_five:
                            manager.setRunningAverage(5);
                            break;

                        case R.id.radio_ten:
                            manager.setRunningAverage(10);
                            break;

                        case R.id.radio_hundred:
                            manager.setRunningAverage(100);
                            break;

                        default:
                            break;
                    }
                    BusManager.getInstance().postRunningAverageChangeEvent();
                }
            }
        });
    }

}
