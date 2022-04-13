package exercise1.lib;

public class SimulationController {

	private SimulationModel simModel;
	private int nWorkers;
	private MasterService masterService;
	private StopFlag stopFlag;

	public SimulationController(SimulationModel simModel, int nWorkers) {
		this.simModel = simModel;
		this.nWorkers = nWorkers;
		this.stopFlag = new StopFlag(false);
	}

	public void stop(){
		if(masterService != null){
			System.out.println("Stop invoked");
			stopFlag.setStopFlag(true);
		}
	}

	public void restart(){
		simModel.reset();
		stopFlag.setStopFlag(false);
		masterService = new MasterService(simModel, nWorkers, stopFlag);
		masterService.start();
	}

	public void execute() {
        masterService = new MasterService(simModel, nWorkers, stopFlag);
        masterService.start();
    }
}
