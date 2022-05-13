package org.egov.client.edcr;

import static org.egov.client.constants.DxfFileConstants_AR.C1a;
import static org.egov.client.constants.DxfFileConstants_AR.C1b;
import static org.egov.client.constants.DxfFileConstants_AR.C1c;
import static org.egov.client.constants.DxfFileConstants_AR.C2a;
import static org.egov.client.constants.DxfFileConstants_AR.C3a;
import static org.egov.client.constants.DxfFileConstants_AR.C4a;
import static org.egov.client.constants.DxfFileConstants_AR.C5a;
import static org.egov.client.constants.DxfFileConstants_AR.C6a;
import static org.egov.client.constants.DxfFileConstants_AR.I;
import static org.egov.client.constants.DxfFileConstants_AR.I1a;
import static org.egov.client.constants.DxfFileConstants_AR.I1b;
import static org.egov.client.constants.DxfFileConstants_AR.P;
import static org.egov.client.constants.DxfFileConstants_AR.P3c;
import static org.egov.client.constants.DxfFileConstants_AR.R;
import static org.egov.client.constants.DxfFileConstants_AR.R1a;
import static org.egov.client.constants.DxfFileConstants_AR.R1b;
import static org.egov.client.constants.DxfFileConstants_AR.R1c;
import static org.egov.client.constants.DxfFileConstants_AR.R2a;
import static org.egov.client.constants.DxfFileConstants_AR.T;
import static org.egov.edcr.constants.DxfFileConstants.C;
import static org.egov.edcr.constants.DxfFileConstants.G;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.common.entity.edcr.Block;
import org.egov.common.entity.edcr.Floor;
import org.egov.common.entity.edcr.Measurement;
import org.egov.common.entity.edcr.Occupancy;
import org.egov.common.entity.edcr.OccupancyTypeHelper;
import org.egov.common.entity.edcr.Plan;
import org.egov.common.entity.edcr.Result;
import org.egov.common.entity.edcr.ScrutinyDetail;
import org.egov.edcr.feature.PlotArea;
import org.egov.infra.utils.StringUtils;
import org.springframework.stereotype.Service;



@Service
public class PlotArea_AR extends PlotArea{
	private static final String SQMTRS = " m²";
	private static final Logger LOG = Logger.getLogger(PlotArea_AR.class);
	private static final BigDecimal PLOT_AREA_48 = BigDecimal.valueOf(48);
	private static final BigDecimal PLOT_AREA_60 = BigDecimal.valueOf(60);
	private static final BigDecimal PLOT_AREA_100 = BigDecimal.valueOf(100);
	private static final BigDecimal PLOT_AREA_250 = BigDecimal.valueOf(250);
	private static final BigDecimal PLOT_AREA_500 = BigDecimal.valueOf(500);
	private static final BigDecimal PLOT_AREA_1000 = BigDecimal.valueOf(1000);
	private static final BigDecimal PLOT_AREA_1500 = BigDecimal.valueOf(1500);
	private static final BigDecimal PLOT_AREA_3000 = BigDecimal.valueOf(3000);
	private static final BigDecimal PLOT_AREA_5000 = BigDecimal.valueOf(5000);
	private static final BigDecimal PLOT_AREA_2000 = BigDecimal.valueOf(2000);
	private static final BigDecimal PLOT_AREA_4000 = BigDecimal.valueOf(4000);
	private static final BigDecimal PLOT_AREA_12000 = BigDecimal.valueOf(12000);
	private static final BigDecimal PLOT_AREA_28000 = BigDecimal.valueOf(28000);
	private static final BigDecimal PLOT_AREA_6000 = BigDecimal.valueOf(6000);
	private static final BigDecimal PLOT_AREA_20000 = BigDecimal.valueOf(20000);
	private static final BigDecimal PLOT_AREA_10000 = BigDecimal.valueOf(10000);
	private static final BigDecimal PLOT_AREA_1080 = BigDecimal.valueOf(1080);
	private static final BigDecimal PLOT_AREA_510 = BigDecimal.valueOf(510);
	private static final BigDecimal PLOT_AREA_400 = BigDecimal.valueOf(400);
	
	@Override
	public Plan validate(Plan pl) {
		OccupancyTypeHelper mostRestrictiveOccupancyType = Util_AR.getMostRestrictive(pl);
		HashMap<String, String> errors = new HashMap<>();
		for (Block block : pl.getBlocks()) {
			for (Occupancy occupancy : block.getBuilding().getOccupancies()) {
				if (occupancy.getTypeHelper().getType() != null) {

					scrutinyDetail.setKey("Common_Plot Area");

					if (pl.getPlot() != null && (pl.getPlot().getArea() != null
							|| pl.getPlot().getArea().compareTo(BigDecimal.ZERO) > 0)) {

						BigDecimal maxPlotArea= BigDecimal.ZERO;
						BigDecimal minPlotArea = BigDecimal.ZERO;
						BigDecimal plotArea = pl.getPlot().getArea();

						if (mostRestrictiveOccupancyType != null) {
							if ((mostRestrictiveOccupancyType.getType() != null
									&& R.equalsIgnoreCase(mostRestrictiveOccupancyType.getType().getCode()))) {
								processPlotAreaResidential(pl, mostRestrictiveOccupancyType,  plotArea,maxPlotArea,minPlotArea,errors);
							}
							if (mostRestrictiveOccupancyType.getType() != null
									&& C.equalsIgnoreCase(mostRestrictiveOccupancyType.getType().getCode())) {
								processPlotAreaCommercial(pl, mostRestrictiveOccupancyType,  plotArea,maxPlotArea,minPlotArea,errors);
							}
							if (mostRestrictiveOccupancyType.getType() != null
									&& I.equalsIgnoreCase(mostRestrictiveOccupancyType.getType().getCode())) {

								processPlotAreaIndustrial(pl, mostRestrictiveOccupancyType,  plotArea,maxPlotArea,minPlotArea,errors);
							}
							if (mostRestrictiveOccupancyType.getType() != null
									&& G.equalsIgnoreCase(mostRestrictiveOccupancyType.getType().getCode())) {

								processPlotAreaGovernmentUse(pl, mostRestrictiveOccupancyType,  plotArea,maxPlotArea,minPlotArea,errors);
							}
							if (mostRestrictiveOccupancyType.getType() != null
									&& T.equalsIgnoreCase(mostRestrictiveOccupancyType.getType().getCode())) {

								processPlotAreaTransportation(pl, mostRestrictiveOccupancyType,  plotArea,maxPlotArea,minPlotArea,errors);
							}
							if (mostRestrictiveOccupancyType.getType() != null
									&& P.equalsIgnoreCase(mostRestrictiveOccupancyType.getType().getCode())) {

								processPlotAreaPublicSemiPublic(pl, mostRestrictiveOccupancyType,  plotArea,maxPlotArea,minPlotArea,errors);
							}

						}
						
						
					}
				}
			}
		}
		return pl;
	}

	

	private void processPlotAreaResidential(Plan pl, OccupancyTypeHelper mostRestrictiveOccupancyType,
			BigDecimal PlotArea, BigDecimal maxPlotArea,BigDecimal minPlotArea,HashMap<String, String> errors) {
		int flag=0;
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(R1b)
				|| mostRestrictiveOccupancyType.getSubtype().getCode().equals(R1c)) {
				minPlotArea = PLOT_AREA_48;
				maxPlotArea=PLOT_AREA_3000;
		}
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(R2a)){
			minPlotArea = PLOT_AREA_10000;
			flag=1;
		}
		if (errors.isEmpty() && minPlotArea.compareTo(BigDecimal.ZERO)>0) {
			buildResult(pl, mostRestrictiveOccupancyType,PlotArea, minPlotArea, maxPlotArea, flag);
		}
		
	}

	private void processPlotAreaCommercial(Plan pl, OccupancyTypeHelper mostRestrictiveOccupancyType,
			BigDecimal plotArea, BigDecimal maxPlotArea,BigDecimal minPlotArea,HashMap<String, String> errors) {
		int flag=0;
		// Restaurants/shops
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(C1a)
				|| mostRestrictiveOccupancyType.getSubtype().getCode().equals(C1b)) {
			minPlotArea = PLOT_AREA_48;
			maxPlotArea=PLOT_AREA_3000;
		}
		
		//Shopping Mall
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(C1c)) {
			minPlotArea = BigDecimal.valueOf(450);
			flag=1;
		}
		
		// Hotel
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(C2a)) {
			minPlotArea = PLOT_AREA_250;
			maxPlotArea=PLOT_AREA_1500;
		}
	
		// Petrol Pump
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(C5a)) {
			minPlotArea = PLOT_AREA_510;
			flag=1;
		}
		if (errors.isEmpty()&&minPlotArea.compareTo(BigDecimal.ZERO)>0) {
			buildResult(pl, mostRestrictiveOccupancyType,plotArea, minPlotArea, maxPlotArea, flag);
		}
		
		
	}



	private void processPlotAreaIndustrial(Plan pl, OccupancyTypeHelper mostRestrictiveOccupancyType,
			BigDecimal plotArea, BigDecimal maxPlotArea,BigDecimal minPlotArea,HashMap<String, String> errors) {
		int flag=0;
				// Flatted
				if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(I1a)) {
					minPlotArea = PLOT_AREA_2000;
					flag=1;
				}

				// Light and Service
				if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(I1b)) {
					minPlotArea = PLOT_AREA_400;
					flag=1;
				}
				if (errors.isEmpty()&&minPlotArea.compareTo(BigDecimal.ZERO)>0) {
					buildResult(pl, mostRestrictiveOccupancyType,plotArea, minPlotArea, maxPlotArea, flag);
				}
	}



	private void processPlotAreaGovernmentUse(Plan pl, OccupancyTypeHelper mostRestrictiveOccupancyType,
			BigDecimal plotArea, BigDecimal maxPlotArea,BigDecimal minPlotArea,HashMap<String, String> errors) {
		// TODO Auto-generated method stub
		
	}



	private void processPlotAreaTransportation(Plan pl, OccupancyTypeHelper mostRestrictiveOccupancyType,
			BigDecimal plotArea, BigDecimal maxPlotArea,BigDecimal minPlotArea,HashMap<String, String> errors) {
		// TODO Auto-generated method stub
		
	}



	private void processPlotAreaPublicSemiPublic(Plan pl, OccupancyTypeHelper mostRestrictiveOccupancyType,
			BigDecimal plotArea, BigDecimal maxPlotArea,BigDecimal minPlotArea,HashMap<String, String> errors) {
		int flag=0;
		//Hostel
				if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(P3c)) {
					minPlotArea = PLOT_AREA_100;
					maxPlotArea=PLOT_AREA_3000;
				}
				if (errors.isEmpty()&&minPlotArea.compareTo(BigDecimal.ZERO)>0) {
					buildResult(pl, mostRestrictiveOccupancyType,plotArea, minPlotArea, maxPlotArea, flag);
				}
		
	}
	
	private void buildResult(Plan pl, OccupancyTypeHelper mostRestrictiveOccupancyType,BigDecimal PlotArea, BigDecimal minPlotArea,
			BigDecimal maxPlotArea, int flag) {
		Boolean isAccepted=false;
		ScrutinyDetail scrutinyDetail = new ScrutinyDetail();
		scrutinyDetail.addColumnHeading(1, RULE_NO);
		scrutinyDetail.addColumnHeading(2, OCCUPANCY);
		scrutinyDetail.addColumnHeading(3, "Plot Area");
		scrutinyDetail.addColumnHeading(5, PERMISSIBLE);
		scrutinyDetail.addColumnHeading(7, STATUS);
		scrutinyDetail.setKey("Common_Plot Area");
		String occupancyName;
		if (mostRestrictiveOccupancyType.getSubtype() != null)
			occupancyName = mostRestrictiveOccupancyType.getSubtype().getName();
		else
			occupancyName = mostRestrictiveOccupancyType.getType().getName();

		Map<String, String> details = new HashMap<>();
		details.put(RULE_NO, "TBD");
		details.put(OCCUPANCY, occupancyName);
		if(flag==0) {
			details.put(PERMISSIBLE,">="+minPlotArea.toString() + SQMTRS +" & <="+maxPlotArea.toString() + SQMTRS);
			details.put("Plot Area", PlotArea.toString() + SQMTRS);
			if(PlotArea.compareTo(minPlotArea)>=0 && PlotArea.compareTo(maxPlotArea)<=0) {
				isAccepted=true;
			}
		}else {
			details.put(PERMISSIBLE,">="+minPlotArea.toString() + SQMTRS);
			details.put("Plot Area", PlotArea.toString() + SQMTRS);
			if(PlotArea.compareTo(minPlotArea)>=0) {
				isAccepted=true;
				
			}
		}
		details.put(STATUS, isAccepted ? Result.Accepted.getResultVal() : Result.Not_Accepted.getResultVal());

		scrutinyDetail.getDetail().add(details);
		pl.getReportOutput().getScrutinyDetails().add(scrutinyDetail);
			
	}


	@Override
	public Plan process(Plan plan) {
		validate(plan);
		return plan;
	}

	@Override
	public Map<String, Date> getAmendments() {
		// TODO Auto-generated method stub
		return super.getAmendments();
	
	}
}
