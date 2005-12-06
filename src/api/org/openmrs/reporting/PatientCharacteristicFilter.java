package org.openmrs.reporting;

import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.PatientService;
import java.util.*;

public class PatientCharacteristicFilter implements DataFilter<Patient> {

	Context context;
	String gender; // "M" or "F"
	Integer minAge;
	Integer maxAge;
	
	public PatientCharacteristicFilter() { }
	
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Integer getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(Integer maxAge) {
		this.maxAge = maxAge;
	}

	public Integer getMinAge() {
		return minAge;
	}

	public void setMinAge(Integer minAge) {
		this.minAge = minAge;
	}
	
	public Context getContext() {
		return context;
	}
	
	public void setContext(Context context) {
		this.context = context;
	}

	final static long ms_per_year = 1000l * 60 * 60 * 24 * 365;
	
	public DataSet<Patient> filter(DataSet<Patient> input) {
		PatientService patientService = context.getPatientService();
		DataSet<Patient> toReturn = new SimpleDataSet<Patient>();
		boolean checkGender = gender != null;
		boolean checkAge = minAge != null || maxAge != null;
		int minAgeInt = minAge == null ? -1 : minAge.intValue();
		int maxAgeInt = maxAge == null ? Integer.MAX_VALUE : maxAge.intValue();
		for (Patient patient : input.getRowKeys()) {
			if (checkGender && !gender.equals(patient.getGender())) {
				continue;
			}
			if (checkAge) {
				int age = (int) ((System.currentTimeMillis() - patient.getBirthdate().getTime()) / ms_per_year);
				if (age < minAgeInt || age > maxAgeInt) {
					continue;
				}
			}
			toReturn.setRow(patient, input.getRow(patient));			
		}
		return toReturn;
	}
	
}
