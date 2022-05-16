package exercise3;

import common.ControllerInterface;
import common.Flag;
import common.View;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class Controller implements ControllerInterface {

    private ReactiveJavaParser lib;
    private Flag stopFlag;
    private View view;
    private Disposable d;

    public Controller(ReactiveJavaParser lib) {
        this.lib = lib;
        this.stopFlag = new Flag();
    }

    public void setView(View view) {
        this.view = view;
    }

    @Override
    public void startAnalysis(String srcDirectory) {
        this.stopFlag.reset();
        System.out.println("Controller: start");
        PublishSubject<String> streamReports = PublishSubject.create();
        this.d = streamReports
                .observeOn(Schedulers.computation())
                .subscribe((v) -> {
                    view.notifyUpdates(v);
                });
        lib.analyzeProject(srcDirectory, streamReports, stopFlag);
    }

    @Override
    public void stopAnalysis() {
        System.out.println("Controller: stop");
        this.stopFlag.stop();
        this.d.dispose();
    }
}
