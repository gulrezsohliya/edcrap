package org.egov.client.edcr;

import static org.egov.edcr.constants.DxfFileConstants.A;
import static org.egov.edcr.constants.DxfFileConstants.A_AF;
import static org.egov.edcr.constants.DxfFileConstants.A_FH;
import static org.egov.edcr.constants.DxfFileConstants.F_RT;
import static org.egov.edcr.constants.DxfFileConstants.G;
import static org.egov.edcr.constants.DxfFileConstants.F_CB;

import static org.egov.edcr.constants.DxfFileConstants.A_HE;
import static org.egov.edcr.constants.DxfFileConstants.A_R;
import static org.egov.edcr.constants.DxfFileConstants.F_H;
import static org.egov.edcr.constants.DxfFileConstants.B;
import static org.egov.edcr.constants.DxfFileConstants.C;
import static org.egov.edcr.constants.DxfFileConstants.D;
import static org.egov.edcr.constants.DxfFileConstants.F;
import static org.egov.edcr.constants.DxfFileConstants.I;
import static org.egov.edcr.constants.DxfFileConstants.A_PO;

import static org.egov.edcr.utility.DcrConstants.FRONT_YARD_DESC;
import static org.egov.edcr.utility.DcrConstants.OBJECTNOTDEFINED;

// arunachal constants add
import static org.egov.client.constants.DxfFileConstants_AR.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import org.egov.common.entity.edcr.Block;
import org.egov.common.entity.edcr.Building;
import org.egov.common.entity.edcr.Occupancy;
import org.egov.common.entity.edcr.OccupancyTypeHelper;
import org.egov.common.entity.edcr.Plan;
import org.egov.common.entity.edcr.Plot;
import org.egov.common.entity.edcr.Result;
import org.egov.common.entity.edcr.ScrutinyDetail;
import org.egov.common.entity.edcr.SetBack;
import org.egov.edcr.feature.FrontYardService;
import org.springframework.stereotype.Service;

@Service
public class FrontYardService_AR extends FrontYardService {
	private static final String RULE_35 = "35 Table-8";
	private static final String RULE_36 = "36";
	private static final String RULE_37_TWO_A = "37-2-A";
	private static final String RULE_37_TWO_B = "37-2-B";
	private static final String RULE_37_TWO_C = "37-2-C";
	private static final String RULE_37_TWO_D = "37-2-D";
	private static final String RULE_37_TWO_G = "37-2-G";
	private static final String RULE_37_TWO_H = "37-2-H";
	private static final String RULE_37_TWO_I = "37-2-I";
	private static final String RULE_47 = "47";

	private static final String MINIMUMLABEL = "Minimum distance ";

	private static final BigDecimal FRONTYARDMINIMUM_DISTANCE_1_5 = BigDecimal.valueOf(1.5);
	private static final BigDecimal FRONTYARDMINIMUM_DISTANCE_1_8 = BigDecimal.valueOf(1.8);
	private static final BigDecimal FRONTYARDMINIMUM_DISTANCE_2_5 = BigDecimal.valueOf(2.5);

	private static final BigDecimal FRONTYARDMINIMUM_DISTANCE_3_6 = BigDecimal.valueOf(3.6);
	private static final BigDecimal FRONTYARDMINIMUM_DISTANCE_4 = BigDecimal.valueOf(4);
	private static final BigDecimal FRONTYARDMINIMUM_DISTANCE_4_5 = BigDecimal.valueOf(4.5);
	private static final BigDecimal FRONTYARDMINIMUM_DISTANCE_5 = BigDecimal.valueOf(5);
	private static final BigDecimal FRONTYARDMINIMUM_DISTANCE_5_5 = BigDecimal.valueOf(5.5);
	private static final BigDecimal FRONTYARDMINIMUM_DISTANCE_6 = BigDecimal.valueOf(6);
	private static final BigDecimal FRONTYARDMINIMUM_DISTANCE_6_5 = BigDecimal.valueOf(6.5);
	private static final BigDecimal FRONTYARDMINIMUM_DISTANCE_7 = BigDecimal.valueOf(7);
	private static final BigDecimal FRONTYARDMINIMUM_DISTANCE_7_5 = BigDecimal.valueOf(7.5);
	private static final BigDecimal FRONTYARDMINIMUM_DISTANCE_8 = BigDecimal.valueOf(8);
	private static final BigDecimal FRONTYARDMINIMUM_DISTANCE_9 = BigDecimal.valueOf(9);
	private static final BigDecimal FRONTYARDMINIMUM_DISTANCE_10 = BigDecimal.valueOf(10);
	private static final BigDecimal FRONTYARDMINIMUM_DISTANCE_11 = BigDecimal.valueOf(11);
	private static final BigDecimal FRONTYARDMINIMUM_DISTANCE_12 = BigDecimal.valueOf(12);
	private static final BigDecimal FRONTYARDMINIMUM_DISTANCE_13 = BigDecimal.valueOf(13);
	private static final BigDecimal FRONTYARDMINIMUM_DISTANCE_14 = BigDecimal.valueOf(14);
	private static final BigDecimal FRONTYARDMINIMUM_DISTANCE_15 = BigDecimal.valueOf(15);
	public static final BigDecimal ROAD_WIDTH_TWELVE_POINTTWO = BigDecimal.valueOf(12.2);

	// Arunachal front yard minimum
	private static final BigDecimal FRONTYARDMINIMUM_DISTANCE_2 = BigDecimal.valueOf(2);
	private static final BigDecimal FRONTYARDMINIMUM_DISTANCE_3 = BigDecimal.valueOf(3);
	private static final BigDecimal FRONTYARDMINIMUM_DISTANCE_30 = BigDecimal.valueOf(30);

	public static final String BSMT_FRONT_YARD_DESC = "Basement Front Yard";
	private static final int PLOTAREA_300 = 300;

	private static final BigDecimal PLOT_AREA_48 = BigDecimal.valueOf(48);
	private static final BigDecimal PLOT_AREA_60 = BigDecimal.valueOf(60);
	private static final BigDecimal PLOT_AREA_100 = BigDecimal.valueOf(100);
	private static final BigDecimal PLOT_AREA_250 = BigDecimal.valueOf(250);
	private static final BigDecimal PLOT_AREA_500 = BigDecimal.valueOf(500);
	private static final BigDecimal PLOT_AREA_1000 = BigDecimal.valueOf(1000);
	private static final BigDecimal PLOT_AREA_1500 = BigDecimal.valueOf(1500);
	private static final BigDecimal PLOT_AREA_3000 = BigDecimal.valueOf(3000);
	private static final BigDecimal PLOT_AREA_10000 = BigDecimal.valueOf(10000);
	private static final BigDecimal PLOT_AREA_20000 = BigDecimal.valueOf(20000);
	private static final BigDecimal PLOT_AREA_1080 = BigDecimal.valueOf(20000);
	private static final BigDecimal PLOT_AREA_510 = BigDecimal.valueOf(20000);
	private static final BigDecimal PLOT_AREA_2000 = BigDecimal.valueOf(2000);
	private static final BigDecimal PLOT_AREA_400 = BigDecimal.valueOf(400);
	private static final BigDecimal PLOT_AREA_4000 = BigDecimal.valueOf(4000);
	private static final BigDecimal PLOT_AREA_12000 = BigDecimal.valueOf(12000);
	private static final BigDecimal PLOT_AREA_450 = BigDecimal.valueOf(450);

	private static final Logger LOG = Logger.getLogger(FrontYardService_AR.class);

	private class FrontYardResult {
		String rule;
		String subRule;
		String blockName;
		Integer level;
		BigDecimal actualMeanDistance = BigDecimal.ZERO;
		BigDecimal actualMinDistance = BigDecimal.ZERO;
		String occupancy;
		BigDecimal expectedminimumDistance = BigDecimal.ZERO;
		BigDecimal expectedmeanDistance = BigDecimal.ZERO;
		String additionalCondition;
		boolean status = false;
	}

	public void processFrontYard(Plan pl) {
		Plot plot = null;
		if(pl.getPlot()!=null)
			plot = pl.getPlot();
		BigDecimal plotArea= null;
		if(pl.getPlot().getArea()!=null)
			plotArea = pl.getPlot().getArea();
		HashMap<String, String> errors = new HashMap<>();
		
		Boolean OCTYP_FOUND = false;
		Boolean OCTYP_NOTFOUND = false;
		Boolean valid = false;

		if (plot == null)
			return;
		

		validateFrontYard(pl);
		BigDecimal minVal=BigDecimal.ZERO;
		if (plot != null && !pl.getBlocks().isEmpty()) {
			for (Block block : pl.getBlocks()) { // for each block
				OccupancyTypeHelper mostRestrictiveOccupancyType = block.getBuilding().getMostRestrictiveFarHelper();
				ScrutinyDetail scrutinyDetail = new ScrutinyDetail();
				scrutinyDetail.addColumnHeading(1, RULE_NO);
				scrutinyDetail.addColumnHeading(2, LEVEL);
				scrutinyDetail.addColumnHeading(3, OCCUPANCY);
				scrutinyDetail.addColumnHeading(4, FIELDVERIFIED);
				scrutinyDetail.addColumnHeading(5, PERMISSIBLE);
				scrutinyDetail.addColumnHeading(6, PROVIDED);
				scrutinyDetail.addColumnHeading(7, STATUS);
				scrutinyDetail.setHeading(FRONT_YARD_DESC);

				FrontYardResult frontYardResult = new FrontYardResult();
				for (SetBack setback : block.getSetBacks()) {
					BigDecimal min=BigDecimal.ZERO;
					if(setback.getLevel()>=0) {
						if (setback.getFrontYard() != null) {
							if(setback.getFrontYard().getMinimumDistance()!=null) 
								min = setback.getFrontYard().getMinimumDistance();
								OCTYP_FOUND = false;
								OCTYP_NOTFOUND = false;
								Occupancy occupancy = new Occupancy();
								occupancy.setTypeHelper(block.getBuilding().getMostRestrictiveFarHelper());
									scrutinyDetail.setKey("Block_" + block.getName() + "_" + FRONT_YARD_DESC);
									if (occupancy.getTypeHelper().getType() == null) {
										OCTYP_NOTFOUND = true; // occ typ not found
									} else {
										OCTYP_FOUND = true; // search for occ typ
										if (mostRestrictiveOccupancyType != null) {
											if ((mostRestrictiveOccupancyType.getType() != null
													&& R.equalsIgnoreCase(mostRestrictiveOccupancyType.getType().getCode()))) {
												frontYardResult =frontSetbackResidential(setback, block.getBuilding(), pl,
														block, setback.getLevel(), plot,plotArea, FRONT_YARD_DESC, min, minVal,
														occupancy.getTypeHelper(), frontYardResult,valid,errors);
											}
											if (mostRestrictiveOccupancyType.getType() != null
													&& C.equalsIgnoreCase(mostRestrictiveOccupancyType.getType().getCode())) {
												frontYardResult =frontSetbackCommercial(setback, block.getBuilding(), pl,
														block, setback.getLevel(), plot,plotArea, FRONT_YARD_DESC, min, minVal,
														occupancy.getTypeHelper(), frontYardResult,valid,errors);
											}
											if (mostRestrictiveOccupancyType.getType() != null
													&& I.equalsIgnoreCase(mostRestrictiveOccupancyType.getType().getCode())) {

												frontYardResult =frontSetbackIndustrial(setback, block.getBuilding(), pl,
														block, setback.getLevel(), plot,plotArea, FRONT_YARD_DESC, min, minVal,
														occupancy.getTypeHelper(), frontYardResult,valid,errors);
											}
											if (mostRestrictiveOccupancyType.getType() != null
													&& G.equalsIgnoreCase(mostRestrictiveOccupancyType.getType().getCode())) {

												frontYardResult =frontSetbackGovernmentUse(setback, block.getBuilding(), pl,
														block, setback.getLevel(), plot,plotArea, FRONT_YARD_DESC, min, minVal,
														occupancy.getTypeHelper(), frontYardResult,valid,errors);
											}
											if (mostRestrictiveOccupancyType.getType() != null
													&& T.equalsIgnoreCase(mostRestrictiveOccupancyType.getType().getCode())) {

												frontYardResult =frontSetbackTransportation(setback, block.getBuilding(), pl,
														block, setback.getLevel(), plot,plotArea, FRONT_YARD_DESC, min, minVal,
														occupancy.getTypeHelper(), frontYardResult,valid,errors);
											}
											if (mostRestrictiveOccupancyType.getType() != null
													&& P.equalsIgnoreCase(mostRestrictiveOccupancyType.getType().getCode())) {

												frontYardResult =frontSetbackPublicSemiPublic(setback, block.getBuilding(), pl,
														block, setback.getLevel(), plot,plotArea, FRONT_YARD_DESC, min, minVal,
														occupancy.getTypeHelper(), frontYardResult,valid,errors);
											}

										}
									}
								
								if (OCTYP_NOTFOUND == true && OCTYP_FOUND == false) {
									errors.put("Block_" + block.getName() + "_" + FRONT_YARD_DESC, FRONT_YARD_DESC
											+ " for Block " + block.getName() + " : Occupancy Type not Found!!");
									pl.addErrors(errors);
								}
							}
							System.out.println("Errors------------->"+pl.getErrors());
							if (errors.isEmpty() && (pl.getErrors()==null)||pl.getErrors().isEmpty()) {
								Map<String, String> details = new HashMap<>();
								details.put(RULE_NO, frontYardResult.subRule);
								details.put(LEVEL, frontYardResult.level != null ? frontYardResult.level.toString() : "");
								details.put(OCCUPANCY, frontYardResult.occupancy);
								details.put(FIELDVERIFIED, MINIMUMLABEL);
								details.put(PERMISSIBLE, frontYardResult.expectedminimumDistance.toString());
								details.put(PROVIDED, frontYardResult.actualMinDistance.toString());

								if (frontYardResult.status) {
									details.put(STATUS, Result.Accepted.getResultVal());
								} else {
									details.put(STATUS, Result.Not_Accepted.getResultVal());
								}
								scrutinyDetail.getDetail().add(details);
								pl.getReportOutput().getScrutinyDetails().add(scrutinyDetail);
							}
					}
					}
				}
			}
		}
	private FrontYardResult frontSetbackPublicSemiPublic(SetBack setback, Building building, Plan pl, Block block, Integer level,
			Plot plot,BigDecimal PlotArea, String frontYardDesc, BigDecimal min, BigDecimal minVal, OccupancyTypeHelper mostRestrictiveOccupancyType,
			FrontYardResult frontYardResult,Boolean valid, HashMap<String, String> errors) {
		String rule="TBD";
		String subRule="TBD";
		//Hostel
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(P3c)) {
			if(PlotArea.compareTo(PLOT_AREA_100)<0) {
				errors.put("PlotArea below 100 Hostel","Plot area below 100 sqmts not allowed for hostel");
				pl.addErrors(errors);
			}
			else if (PlotArea.compareTo(PLOT_AREA_100) > 0 && PlotArea.compareTo(PLOT_AREA_250) <= 0) {
				minVal=FRONTYARDMINIMUM_DISTANCE_3;
			} else if (PlotArea.compareTo(PLOT_AREA_250) > 0 && PlotArea.compareTo(PLOT_AREA_500) <= 0) {
				minVal=FRONTYARDMINIMUM_DISTANCE_3;

			} else if (PlotArea.compareTo(PLOT_AREA_500) > 0 && PlotArea.compareTo(PLOT_AREA_1000) <= 0) {
				minVal=FRONTYARDMINIMUM_DISTANCE_3;
			} else if (PlotArea.compareTo(PLOT_AREA_1000) > 0 && PlotArea.compareTo(PLOT_AREA_1500) <= 0) {
				minVal=FRONTYARDMINIMUM_DISTANCE_3;
			} else if (PlotArea.compareTo(PLOT_AREA_1500) > 0 && PlotArea.compareTo(PLOT_AREA_3000) <= 0) {
				minVal=FRONTYARDMINIMUM_DISTANCE_3;
			}else if(PlotArea.compareTo(PLOT_AREA_3000)>0) {
				errors.put("PlotArea Above 3000 hostel","Plot area should not exceed above 3000 sqmts for hostel");
				pl.addErrors(errors);
			}

		}
		valid = validateMinimumValue(min, minVal);
		return compareFrontYardResult(block.getName(), min,  mostRestrictiveOccupancyType, frontYardResult, valid, subRule,
				rule, minVal,level);
		
	}
	private FrontYardResult frontSetbackTransportation(SetBack setback, Building building, Plan pl, Block block, Integer level,
			Plot plot,BigDecimal plotArea, String frontYardDesc, BigDecimal min, BigDecimal minVal, OccupancyTypeHelper typeHelper,
			FrontYardResult frontYardResult,Boolean valid, HashMap<String, String> errors) {
			return frontYardResult;
		
	}
	private FrontYardResult frontSetbackGovernmentUse(SetBack setback, Building building, Plan pl, Block block, Integer level,
			Plot plot,BigDecimal plotArea, String frontYardDesc, BigDecimal min, BigDecimal minVal, OccupancyTypeHelper typeHelper,
			FrontYardResult frontYardResult,Boolean valid, HashMap<String, String> errors) {
			return frontYardResult;
		
	}
	private FrontYardResult frontSetbackIndustrial(SetBack setback, Building building, Plan pl, Block block, Integer level,
			Plot plot,BigDecimal plotArea, String frontYardDesc, BigDecimal min, BigDecimal minVal, OccupancyTypeHelper typeHelper,
			FrontYardResult frontYardResult,Boolean valid, HashMap<String, String> errors) {
			return frontYardResult;
		
	}
	private FrontYardResult frontSetbackCommercial(SetBack setback, Building building, Plan pl, Block block, Integer level,
			Plot plot,BigDecimal PlotArea, String frontYardDesc, BigDecimal min, BigDecimal minVal, OccupancyTypeHelper mostRestrictiveOccupancyType,
			FrontYardResult frontYardResult,Boolean valid, HashMap<String, String> errors) {
		String rule="TBD";
		String subRule="TBD";
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(C1a)
				|| mostRestrictiveOccupancyType.getSubtype().getCode().equals(C1b)) {
			if(PlotArea.compareTo(PLOT_AREA_48)<0) {
				errors.put("PlotArea below 48 commmercial","Plot area below 48 sqmts not allowed for Shops/Restaurant");
				pl.addErrors(errors);
			}
			else if (PlotArea.compareTo(PLOT_AREA_48) > 0 && PlotArea.compareTo(PLOT_AREA_100) <= 0) {
				minVal=FRONTYARDMINIMUM_DISTANCE_2;
			} else if (PlotArea.compareTo(PLOT_AREA_100) > 0 && PlotArea.compareTo(PLOT_AREA_250) <= 0) {
				minVal=FRONTYARDMINIMUM_DISTANCE_3;
			} else if (PlotArea.compareTo(PLOT_AREA_250) > 0 && PlotArea.compareTo(PLOT_AREA_500) <= 0) {
				minVal=FRONTYARDMINIMUM_DISTANCE_3;

			} else if (PlotArea.compareTo(PLOT_AREA_500) > 0 && PlotArea.compareTo(PLOT_AREA_1000) <= 0) {
				minVal=FRONTYARDMINIMUM_DISTANCE_3;
			} else if (PlotArea.compareTo(PLOT_AREA_1000) > 0 && PlotArea.compareTo(PLOT_AREA_1500) <= 0) {
				minVal=FRONTYARDMINIMUM_DISTANCE_3;
			} else if (PlotArea.compareTo(PLOT_AREA_1500) > 0 && PlotArea.compareTo(PLOT_AREA_3000) <= 0) {
				minVal=FRONTYARDMINIMUM_DISTANCE_3;
			}else if(PlotArea.compareTo(PLOT_AREA_3000)>0) {
				errors.put("PlotArea Above 3000 Commercial","Plot area should not exceed above 3000 sqmts for Restaurant/Shops");
				pl.addErrors(errors);
			}

		}
		// Petrol Pump
				if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(C5a)) {
					if (PlotArea.compareTo(PLOT_AREA_1080) > 0) {
						minVal=FRONTYARDMINIMUM_DISTANCE_30;
					} else if (PlotArea.compareTo(PLOT_AREA_510) > 0) {
						minVal=FRONTYARDMINIMUM_DISTANCE_30;

					}

				}else {
					errors.put("PlotArea below 510 Petrol","Plot area below 510 sqmts not allowed for Petrol Pump");
					pl.addErrors(errors);
				}
		valid = validateMinimumValue(min, minVal);
		return compareFrontYardResult(block.getName(), min,  mostRestrictiveOccupancyType, frontYardResult, valid, subRule,
				rule, minVal,level);
		
	}
	private FrontYardResult frontSetbackResidential(SetBack setback, Building building, Plan pl, Block block, Integer level,
			Plot plot,BigDecimal PlotArea, String frontYardDesc, BigDecimal min, BigDecimal minVal, OccupancyTypeHelper mostRestrictiveOccupancyType,
			FrontYardResult frontYardResult,Boolean valid, HashMap<String, String> errors) {
		String rule="TBD";
		String subRule="TBD";
		
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(R1a)){
			minVal=FRONTYARDMINIMUM_DISTANCE_2;
		}
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(R1b)
				|| mostRestrictiveOccupancyType.getSubtype().getCode().equals(R1c)) {
			if(PlotArea.compareTo(PLOT_AREA_48)<0) {
				errors.put("PlotArea below 48 Residential","Plot area below 48 sqmts not allowed for plotted residential housing");
				pl.addErrors(errors);
			}
			else if (PlotArea.compareTo(PLOT_AREA_48) > 0 && PlotArea.compareTo(PLOT_AREA_60) <= 0) {
				minVal=FRONTYARDMINIMUM_DISTANCE_2;
			} else if (PlotArea.compareTo(PLOT_AREA_60) > 0 && PlotArea.compareTo(PLOT_AREA_100) <= 0) {
				minVal=FRONTYARDMINIMUM_DISTANCE_2;

			} else if (PlotArea.compareTo(PLOT_AREA_100) > 0 && PlotArea.compareTo(PLOT_AREA_250) <= 0) {
				minVal=FRONTYARDMINIMUM_DISTANCE_3;
			} else if (PlotArea.compareTo(PLOT_AREA_250) > 0 && PlotArea.compareTo(PLOT_AREA_500) <= 0) {
				minVal=FRONTYARDMINIMUM_DISTANCE_3;
			} else if (PlotArea.compareTo(PLOT_AREA_500) > 0 && PlotArea.compareTo(PLOT_AREA_1000) <= 0) {
				minVal=FRONTYARDMINIMUM_DISTANCE_3;
			} else if (PlotArea.compareTo(PLOT_AREA_1000) > 0 && PlotArea.compareTo(PLOT_AREA_1500) <= 0) {
				minVal=FRONTYARDMINIMUM_DISTANCE_3;
			} else if (PlotArea.compareTo(PLOT_AREA_1500) > 0 && PlotArea.compareTo(PLOT_AREA_3000) <= 0) {
				minVal=FRONTYARDMINIMUM_DISTANCE_3;
			}else if (PlotArea.compareTo(PLOT_AREA_3000) > 0) {
				errors.put("PlotArea Above 3000 Residential","Plot area should not exceed above 3000 sqmts for plotted residential housing");
				pl.addErrors(errors);
			}
		}
		valid = validateMinimumValue(min, minVal);
		return compareFrontYardResult(block.getName(), min,  mostRestrictiveOccupancyType, frontYardResult, valid, subRule,
				rule, minVal,level);
		
	}
	

	private void validateFrontYard(Plan pl) {

		// Front yard may not be mandatory at each level. We can check whether in any
		// level front yard defined or not ?

		for (Block block : pl.getBlocks()) {
			if (!block.getCompletelyExisting()) {
				Boolean frontYardDefined = false;
				for (SetBack setback : block.getSetBacks()) {
					if (setback.getFrontYard() != null
							&& setback.getFrontYard().getMinimumDistance().compareTo(BigDecimal.valueOf(0)) > 0) {
						frontYardDefined = true;
					}
				}
				if (!frontYardDefined) {
					HashMap<String, String> errors = new HashMap<>();
					errors.put(FRONT_YARD_DESC,
							prepareMessage(OBJECTNOTDEFINED, FRONT_YARD_DESC + " for Block " + block.getName()));
					pl.addErrors(errors);
				}
			}

		}

	}



	private FrontYardResult compareFrontYardResult(String blockName, BigDecimal min,
			OccupancyTypeHelper mostRestrictiveOccupancy, FrontYardResult frontYardResult, Boolean valid,
			String subRule, String rule, BigDecimal minVal,Integer level) {
		String occupancyName;
		if (mostRestrictiveOccupancy.getSubtype() != null)
			occupancyName = mostRestrictiveOccupancy.getSubtype().getName();
		else
			occupancyName = mostRestrictiveOccupancy.getType().getName();
		if (minVal.compareTo(frontYardResult.expectedminimumDistance) >= 0) {
			if (minVal.compareTo(frontYardResult.expectedminimumDistance) == 0) {
				frontYardResult.rule = frontYardResult.rule != null ? frontYardResult.rule + "," + rule : rule;
				frontYardResult.occupancy = frontYardResult.occupancy != null
						? frontYardResult.occupancy + "," + occupancyName
						: occupancyName;
			} else {
				frontYardResult.rule = rule;
				frontYardResult.occupancy = occupancyName;
			}

			frontYardResult.subRule = subRule;
			frontYardResult.blockName = blockName;
			frontYardResult.level = level;
			frontYardResult.expectedminimumDistance = minVal;
			frontYardResult.actualMinDistance = min;
			frontYardResult.status = valid;

		}
		return frontYardResult;
	}



	private Boolean validateMinimumValue(BigDecimal min, BigDecimal minval) {
		Boolean valid = false;
		if (min.compareTo(minval) >= 0) {
			valid = true;
		}
		return valid;
	}
}
