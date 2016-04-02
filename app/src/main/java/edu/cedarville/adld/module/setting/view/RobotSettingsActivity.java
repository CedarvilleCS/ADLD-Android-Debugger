package edu.cedarville.adld.module.setting.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.cedarville.adld.R;
import edu.cedarville.adld.common.base.BaseActivity;
import edu.cedarville.adld.common.dagger.Components;
import edu.cedarville.adld.module.setting.presenter.RobotSettingsEventHandler;

public class RobotSettingsActivity extends BaseActivity implements RobotSettingsView {


    //------------------------------------------------------------------------------
    // Activity Intent Factory
    //------------------------------------------------------------------------------
    public static Intent instance(Context context) {
        return new Intent(context, RobotSettingsActivity.class);
    }



    //------------------------------------------------------------------------------
    // Activity Dependencies
    //------------------------------------------------------------------------------
    @Inject
    RobotSettingsEventHandler eventHandler;



    //------------------------------------------------------------------------------
    // Activity Views
    //------------------------------------------------------------------------------
    @Bind(R.id.text_robot_name)
    TextView txtRobotName;

    @Bind(R.id.text_robot_address)
    TextView txtRobotAddress;

    @Bind(R.id.checkbox_display_hex)
    CheckBox checkBoxDisplayHex;

    @Bind(R.id.edit_threshold)
    EditText editThreshold;


    //------------------------------------------------------------------------------
    // Activity Lifecycle
    //------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robot_settings);
        ButterKnife.bind(this);

        this.eventHandler.attachView(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        this.eventHandler.onViewStarted();
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.eventHandler.onViewStopped();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.eventHandler.detachView();
    }

    //------------------------------------------------------------------------------
    // Abstract Base Methods
    //------------------------------------------------------------------------------
    @Override
    protected void setupDependencies(Components components) {
        components.getRobotComponent().inject(this);
    }



    //------------------------------------------------------------------------------
    // ButterKnife Event Injections
    //------------------------------------------------------------------------------
    @OnClick (R.id.text_robot_name)
    void onEditRobotNameClicked() {
        this.displayEditNameDialog();
    }

    @OnClick(R.id.button_done)
    void onDoneClicked() {
        String robotName = this.txtRobotName.getText().toString();
        String threshold = this.editThreshold.getText().toString();
        boolean displayHex = this.checkBoxDisplayHex.isChecked();

        this.eventHandler.saveSettings(robotName, threshold, displayHex);
    }


    //------------------------------------------------------------------------------
    // Robot Settings View
    //------------------------------------------------------------------------------
    @Override
    public void dismissView() {
        this.finish();
    }

    @Override
    public void setSettingsViewModel(SettingsViewModel viewModel) {
        this.txtRobotName.setText(viewModel.getRobotName());
        this.txtRobotAddress.setText(viewModel.getRobotAddress());
        this.checkBoxDisplayHex.setChecked(viewModel.isDisplayHex());
        this.editThreshold.setText(viewModel.getThreshold());
    }



    //------------------------------------------------------------------------------
    // Private Methods
    //------------------------------------------------------------------------------
    private void displayEditNameDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Robot Name");

        String robotName = this.txtRobotName.getText().toString();

        final EditText input = new EditText(this);
        input.setText(robotName);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);


        alertDialog.setPositiveButton("Save",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String name = input.getText().toString();
                        txtRobotName.setText(name);
                        dialog.dismiss();
                    }
                });

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });


    }
}
