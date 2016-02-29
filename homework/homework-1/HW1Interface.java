package hw1;

import java.util.List;

public interface ICourt {
	public boolean addJurorToPool(String jid);
	public boolean assignJuror(String tid, String jid);
	public Status checkExemptionStatus(String eid);
	public boolean createTrial(int trialid);
	public boolean deleteJurorFromPool(String jid);
	public List<Juror> getJurorosInPool();
	public List<Juror> getAllJurors(String tid);
	public boolean approveExemption(int extemptionId, boolean approved);
	public boolean removeJuror(String tid, String jid);
	public boolean requestExemption(int uid, List<Date> dates, String letter);
	public boolean jurorInTrial(String tid, String jid);
}

public interface IDatabaseSupport {
	public boolean putJuror(Juror j);
	public Trial getTrial(String tid);
	public boolean putTrial(Trial t);
	public Exemption getExemption(String eid);
	public boolean addTrial(Trial t);
	public boolean removeJuror(String jid);
	public List<Juror> getJurorsInPool();
	public Juror getJuror(String jid);
	
}

public interface IJuror {
	public boolean addExemption(List<Date> dates, String letter);
}

public interface ITrial {
	public boolean assignJuror(String jid);
	public List<Juror> getAllJurors();
	public boolean removeJuror(String jid);
	public boolean jurorInTrial(String jid);
}

public interface IExpemtion	{
	public Status getStatus();
	public boolean setApproved(boolean approved);
}

public interface IJuryPool {
	public List<Juror> getJurorsInPool();
}