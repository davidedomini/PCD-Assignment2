package exercise3;

import common.ControllerInterface;
import common.Flag;
import common.View;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class Controller implements ControllerInterface {

    private final ReactiveJavaParser lib;
    private View view;
    private Disposable d;

    public Controller(ReactiveJavaParser lib) {
        this.lib = lib;
    }

    public void setView(View view) {
        this.view = view;
    }

    @Override
    public void startAnalysis(String srcDirectory) {
        System.out.println("Controller: start");
        Observable<String> ob = lib.analyzeProject(srcDirectory);
        this.d = ob
                .observeOn(Schedulers.computation())
                .subscribe(view::notifyUpdates);
    }

    @Override
    public void stopAnalysis() {
        System.out.println("Controller: stop");
        this.d.dispose();
    }
}
