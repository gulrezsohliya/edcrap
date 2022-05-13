package org.egov.client.edcr;

import static org.egov.edcr.constants.DxfFileConstants.A;
import static org.egov.edcr.constants.DxfFileConstants.A_AF;
import static org.egov.edcr.constants.DxfFileConstants.A_FH;
import static org.egov.edcr.constants.DxfFileConstants.F_RT;
import static org.egov.edcr.constants.DxfFileConstants.G;
import static org.egov.edcr.constants.DxfFileConstants.A_HE;
import static org.egov.edcr.constants.DxfFileConstants.A_R;
import static org.egov.edcr.constants.DxfFileConstants.B;
import static org.egov.edcr.constants.DxfFileConstants.C;
import static org.egov.edcr.constants.DxfFileConstants.D;
import static org.egov.edcr.constants.DxfFileConstants.F;
import static org.egov.edcr.constants.DxfFileConstants.F_CB;
import static org.egov.edcr.constants.DxfFileConstants.I;
import static org.egov.edcr.constants.DxfFileConstants.A_PO;
import static org.egov.edcr.utility.DcrConstants.FRONT_YARD_DESC;
import static org.egov.edcr.utility.DcrConstants.OBJECTNOTDEFINED;
import static org.egov.edcr.utility.DcrConstants.REAR_YARD_DESC;
import static org.egov.edcr.utility.DcrConstants.YES;

//arunachal constants add
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
import org.egov.edcr.constants.DxfFileConstants;
import org.egov.edcr.feature.RearYardService;
import org.egov.infra.utils.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class RearYardService_AR extends RearYardService {
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

	private static final BigDecimal REARYARDMINIMUM_DISTANCE_0_9 = BigDecimal.valueOf(0.9);

	private static final BigDecimal REARYARDMINIMUM_DISTANCE_1_5 = BigDecimal.valueOf(1.5);
	private static final BigDecimal REARYARDMINIMUM_DISTANCE_1_8 = BigDecimal.valueOf(1.8);
	private static final BigDecimal REARYARDMINIMUM_DISTANCE_2 = BigDecimal.valueOf(2);
	private static final BigDecimal REARYARDMINIMUM_DISTANCE_2_5 = BigDecimal.valueOf(2.5);
	private static final BigDecimal REARYARDMINIMUM_DISTANCE_3 = BigDecimal.valueOf(3);
	private static final BigDecimal REARYARDMINIMUM_DISTANCE_3_6 = BigDecimal.valueOf(3.6);
	private static final BigDecimal REARYARDMINIMUM_DISTANCE_4 = BigDecimal.valueOf(4);
	private static final BigDecimal REARYARDMINIMUM_DISTANCE_4_5 = BigDecimal.valueOf(4.5);
	private static final BigDecimal REARYARDMINIMUM_DISTANCE_5 = BigDecimal.valueOf(5);
	private static final BigDecimal REARYARDMINIMUM_DISTANCE_6 = BigDecimal.valueOf(6);
	private static final BigDecimal REARYARDMINIMUM_DISTANCE_7 = BigDecimal.valueOf(7);
	private static final BigDecimal REARYARDMINIMUM_DISTANCE_8 = BigDecimal.valueOf(8);
	private static final BigDecimal REARYARDMINIMUM_DISTANCE_9 = BigDecimal.valueOf(9);
	private static final BigDecimal REARYARDMINIMUM_DISTANCE_12 = BigDecimal.valueOf(12);

	// Arunachal front yard minimum
	private static final BigDecimal REARYARDMINIMUM_DISTANCE_1 = BigDecimal.valueOf(1);
	private static final BigDecimal REARYARDMINIMUM_DISTANCE_1_2 = BigDecimal.valueOf(1.2);

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

	public static final String BSMT_REAR_YARD_DESC = "Basement Rear Setback";
	private static final int PLOTAREA_300 = 300;
	public static final BigDecimal ROAD_WIDTH_TWELVE_POINTTWO = BigDecimal.valueOf(12.2);

	private static final Logger LOG = Logger.getLogger(RearYardService_AR.class);

	private class RearYardResult {
		String rule;
		String subRule;
		String blockName;
		Integer level;
		BigDecimal actualMeanDistance = BigDecimal.ZERO;
		BigDecimal actualMinDistance = BigDecimal.ZERO;
		String occupancy;
		BigDecimal expectedminimumDistance = BigDecimal.ZERO;
		BigDecimal expectedmeanDistance = BigDecimal.ZERO;
		boolean status = false;
	}

	public void processRearYard(final Plan pl) {
		HashMap<String, String> errors = new HashMap<>();
		BigDecimal minVal=BigDecimal.ZERO;
		Plot plot = null;
		if(pl.getPlot()!=null)
			plot = pl.getPlot();
		BigDecimal plotArea= null;
		if(pl.getPlot().getArea()!=null)
			plotArea = pl.getPlot().getArea();
		Boolean OCTYP_FOUND = false;
		Boolean OCTYP_NOTFOUND = false;
		Boolean valid = false;

		if (plot == null)
			return;

		validateRearYard(pl);

		if (plot != null && !pl.getBlocks().isEmpty()) {
			for (Block block : pl.getBlocks()) { // for each block
				OccupancyTypeHelper mostRestrictiveOccupancyType = block.getBuilding().getMostRestrictiveFarHelper();
				scrutinyDetail = new ScrutinyDetail();
				scrutinyDetail.addColumnHeading(1, RULE_NO);
				scrutinyDetail.addColumnHeading(2, LEVEL);
				scrutinyDetail.addColumnHeading(3, OCCUPANCY);
				scrutinyDetail.addColumnHeading(4, FIELDVERIFIED);
				scrutinyDetail.addColumnHeading(5, PERMISSIBLE);
				scrutinyDetail.addColumnHeading(6, PROVIDED);
				scrutinyDetail.addColumnHeading(7, STATUS);
				scrutinyDetail.setHeading(REAR_YARD_DESC);
				RearYardResult rearYardResult = new RearYardResult();

				for (SetBack setback : block.getSetBacks()) {
					BigDecimal min=BigDecimal.ZERO;
					if(setback.getLevel()>=0) {
						if (setback.getRearYard() != null
								&& setback.getRearYard().getMinimumDistance().compareTo(BigDecimal.ZERO) > 0) {
								min = setback.getRearYard().getMinimumDistance();
							OCTYP_FOUND = false;
							OCTYP_NOTFOUND = false;
							Occupancy occupancy = new Occupancy();
							occupancy.setTypeHelper(block.getBuilding().getMostRestrictiveFarHelper());
									scrutinyDetail.setKey("Block_" + block.getName() + "_" + "Rear Setback");
									if (occupancy.getTypeHelper().getType() == null) {
										OCTYP_NOTFOUND = true; // occ typ not found
									} else {
										OCTYP_FOUND = true; // search for occ typ
										if (mostRestrictiveOccupancyType != null) {
											if ((mostRestrictiveOccupancyType.getType() != null
													&& R.equalsIgnoreCase(mostRestrictiveOccupancyType.getType().getCode()))) {
												rearYardResult =rearSetbackResidential(setback, block.getBuilding(), pl,
														block, setback.getLevel(), plot,plotArea, FRONT_YARD_DESC, min, minVal,
														occupancy.getTypeHelper(), rearYardResult,valid,errors);
											}
											if (mostRestrictiveOccupancyType.getType() != null
													&& C.equalsIgnoreCase(mostRestrictiveOccupancyType.getType().getCode())) {
												rearYardResult =rearSetbackCommercial(setback, block.getBuilding(), pl,
														block, setback.getLevel(), plot,plotArea, FRONT_YARD_DESC, min, minVal,
														occupancy.getTypeHelper(), rearYardResult,valid,errors);
											}
											if (mostRestrictiveOccupancyType.getType() != null
													&& I.equalsIgnoreCase(mostRestrictiveOccupancyType.getType().getCode())) {

												rearYardResult =rearSetbackIndustrial(setback, block.getBuilding(), pl,
														block, setback.getLevel(), plot,plotArea, FRONT_YARD_DESC, min, minVal,
														occupancy.getTypeHelper(), rearYardResult,valid,errors);
											}
											if (mostRestrictiveOccupancyType.getType() != null
													&& G.equalsIgnoreCase(mostRestrictiveOccupancyType.getType().getCode())) {

												rearYardResult =rearSetbackGovernmentUse(setback, block.getBuilding(), pl,
														block, setback.getLevel(), plot,plotArea, FRONT_YARD_DESC, min, minVal,
														occupancy.getTypeHelper(), rearYardResult,valid,errors);
											}
											if (mostRestrictiveOccupancyType.getType() != null
													&& T.equalsIgnoreCase(mostRestrictiveOccupancyType.getType().getCode())) {

												rearYardResult =rearSetbackTransportation(setback, block.getBuilding(), pl,
														block, setback.getLevel(), plot,plotArea, FRONT_YARD_DESC, min, minVal,
														occupancy.getTypeHelper(), rearYardResult,valid,errors);
											}
											if (mostRestrictiveOccupancyType.getType() != null
													&& P.equalsIgnoreCase(mostRestrictiveOccupancyType.getType().getCode())) {

												rearYardResult =rearSetbackPublicSemiPublic(setback, block.getBuilding(), pl,
														block, setback.getLevel(), plot,plotArea, FRONT_YARD_DESC, min, minVal,
														occupancy.getTypeHelper(), rearYardResult,valid,errors);
											}
										}
									}
								if (OCTYP_NOTFOUND == true && OCTYP_FOUND == false) {
									errors.put("Block_" + block.getName() + "_" + REAR_YARD_DESC, REAR_YARD_DESC
											+ " for Block " + block.getName() + " : Occupancy Type not Found!!");
									pl.addErrors(errors);
								}

								if (errors.isEmpty() && (pl.getErrors()==null)||pl.getErrors().isEmpty()) {
									Map<String, String> details = new HashMap<>();
									details.put(RULE_NO, rearYardResult.subRule);
									details.put(LEVEL, rearYardResult.level != null ? rearYardResult.level.toString() : "");
									details.put(OCCUPANCY, rearYardResult.occupancy);
									if (rearYardResult.expectedmeanDistance != null
											&& rearYardResult.expectedmeanDistance.compareTo(BigDecimal.valueOf(0)) == 0) {
										details.put(FIELDVERIFIED, MINIMUMLABEL);
										details.put(PERMISSIBLE, rearYardResult.expectedminimumDistance.toString());
										details.put(PROVIDED, rearYardResult.actualMinDistance.toString());

									} else {
										details.put(FIELDVERIFIED, MINIMUMLABEL);
										details.put(PERMISSIBLE, rearYardResult.expectedminimumDistance.toString());
										details.put(PROVIDED, rearYardResult.actualMinDistance.toString());
									}
									if (rearYardResult.status) {
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

	}



	private RearYardResult rearSetbackResidential(SetBack setback, Building building, Plan pl, Block block,
			Integer level, Plot plot, BigDecimal PlotArea, String frontYardDesc, BigDecimal min, BigDecimal minVal,
			OccupancyTypeHelper mostRestrictiveOccupancyType, RearYardResult rearYardResult, Boolean valid, HashMap<String, String> errors) {
		String rule="TBD";
		String subRule="TBD";
		
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(R1a)){
			minVal=REARYARDMINIMUM_DISTANCE_1;
		}
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(R1b)
				|| mostRestrictiveOccupancyType.getSubtype().getCode().equals(R1c)) {
			if(PlotArea.compareTo(PLOT_AREA_48)<0) {
				errors.put("PlotArea below 48 Residential","Plot area below 48 sqmts not allowed for plotted residential housing");
				pl.addErrors(errors);
			}
			else if (PlotArea.compareTo(PLOT_AREA_48) > 0 && PlotArea.compareTo(PLOT_AREA_60) <= 0) {
				minVal=REARYARDMINIMUM_DISTANCE_1;
			} else if (PlotArea.compareTo(PLOT_AREA_60) > 0 && PlotArea.compareTo(PLOT_AREA_100) <= 0) {
				minVal=REARYARDMINIMUM_DISTANCE_1;

			} else if (PlotArea.compareTo(PLOT_AREA_100) > 0 && PlotArea.compareTo(PLOT_AREA_250) <= 0) {
				minVal=REARYARDMINIMUM_DISTANCE_1_2;
			} else if (PlotArea.compareTo(PLOT_AREA_250) > 0 && PlotArea.compareTo(PLOT_AREA_500) <= 0) {
				minVal=REARYARDMINIMUM_DISTANCE_1_2;
			} else if (PlotArea.compareTo(PLOT_AREA_500) > 0 && PlotArea.compareTo(PLOT_AREA_1000) <= 0) {
				minVal=REARYARDMINIMUM_DISTANCE_1_2;
			} else if (PlotArea.compareTo(PLOT_AREA_1000) > 0 && PlotArea.compareTo(PLOT_AREA_1500) <= 0) {
				minVal=REARYARDMINIMUM_DISTANCE_1_2;
			} else if (PlotArea.compareTo(PLOT_AREA_1500) > 0 && PlotArea.compareTo(PLOT_AREA_3000) <= 0) {
				minVal=REARYARDMINIMUM_DISTANCE_1_2;
			}else if (PlotArea.compareTo(PLOT_AREA_3000) > 0) {
				errors.put("PlotArea Above 3000 Residential","Plot area should not exceed above 3000 sqmts for plotted residential housing");
				pl.addErrors(errors);
			}
		}
		valid = validateMinimumValue(min, minVal);
		return compareRearYardResult(block.getName(), min,  mostRestrictiveOccupancyType, rearYardResult, valid, subRule,
				rule, minVal,level);
	}



	private RearYardResult rearSetbackCommercial(SetBack setback, Building building, Plan pl, Block block,
			Integer level, Plot plot, BigDecimal PlotArea, String frontYardDesc, BigDecimal min, BigDecimal minVal,
			OccupancyTypeHelper mostRestrictiveOccupancyType, RearYardResult rearYardResult, Boolean valid,HashMap<String, String> errors) {
		String rule="TBD";
		String subRule="TBD";
		
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(C1a)
				|| mostRestrictiveOccupancyType.getSubtype().getCode().equals(C1b)) {
			if(PlotArea.compareTo(PLOT_AREA_48)<0) {
				errors.put("PlotArea below 48 commmercial","Plot area below 48 sqmts not allowed for Shops/Restaurant");
				pl.addErrors(errors);
			}
			else if (PlotArea.compareTo(PLOT_AREA_48) > 0 && PlotArea.compareTo(PLOT_AREA_100) <= 0) {
				minVal=REARYARDMINIMUM_DISTANCE_1_2;
			} else if (PlotArea.compareTo(PLOT_AREA_100) > 0 && PlotArea.compareTo(PLOT_AREA_250) <= 0) {
				minVal=REARYARDMINIMUM_DISTANCE_1_2;
			} else if (PlotArea.compareTo(PLOT_AREA_250) > 0 && PlotArea.compareTo(PLOT_AREA_500) <= 0) {
				minVal=REARYARDMINIMUM_DISTANCE_1_2;

			} else if (PlotArea.compareTo(PLOT_AREA_500) > 0 && PlotArea.compareTo(PLOT_AREA_1000) <= 0) {
				minVal=REARYARDMINIMUM_DISTANCE_1_2;
			} else if (PlotArea.compareTo(PLOT_AREA_1000) > 0 && PlotArea.compareTo(PLOT_AREA_1500) <= 0) {
				minVal=REARYARDMINIMUM_DISTANCE_1_2;
			} else if (PlotArea.compareTo(PLOT_AREA_1500) > 0 && PlotArea.compareTo(PLOT_AREA_3000) <= 0) {
				minVal=REARYARDMINIMUM_DISTANCE_1_2;
			}else if(PlotArea.compareTo(PLOT_AREA_3000)>0) {
				errors.put("PlotArea Above 3000 Commercial","Plot area should not exceed above 3000 sqmts for Restaurant/Shops");
				pl.addErrors(errors);
			}

		}
		valid = validateMinimumValue(min, minVal);
		return compareRearYardResult(block.getName(), min,  mostRestrictiveOccupancyType, rearYardResult, valid, subRule,
				rule, minVal,level);
	}



	private RearYardResult rearSetbackIndustrial(SetBack setback, Building building, Plan pl, Block block,
			Integer level, Plot plot, BigDecimal plotArea, String frontYardDesc, BigDecimal min, BigDecimal minVal,
			OccupancyTypeHelper typeHelper, RearYardResult rearYardResult, Boolean valid,HashMap<String, String> errors) {
		// TODO Auto-generated method stub
		return null;
	}



	private RearYardResult rearSetbackGovernmentUse(SetBack setback, Building building, Plan pl, Block block,
			Integer level, Plot plot, BigDecimal plotArea, String frontYardDesc, BigDecimal min, BigDecimal minVal,
			OccupancyTypeHelper typeHelper, RearYardResult rearYardResult, Boolean valid,HashMap<String, String> errors) {
		// TODO Auto-generated method stub
		return null;
	}



	private RearYardResult rearSetbackTransportation(SetBack setback, Building building, Plan pl, Block block,
			Integer level, Plot plot, BigDecimal plotArea, String frontYardDesc, BigDecimal min, BigDecimal minVal,
			OccupancyTypeHelper typeHelper, RearYardResult rearYardResult, Boolean valid,HashMap<String, String> errors) {
		// TODO Auto-generated method stub
		return null;
	}



	private RearYardResult rearSetbackPublicSemiPublic(SetBack setback, Building building, Plan pl, Block block,
			Integer level, Plot plot, BigDecimal PlotArea, String frontYardDesc, BigDecimal min, BigDecimal minVal,
			OccupancyTypeHelper mostRestrictiveOccupancyType, RearYardResult rearYardResult, Boolean valid,HashMap<String, String> errors) {
		String rule="TBD";
		String subRule="TBD";
		
		//Hostel
				if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(P3c)) {
					if(PlotArea.compareTo(PLOT_AREA_100)<0) {
						errors.put("PlotArea below 100 Hostel","Plot area below 100 sqmts not allowed for hostel");
						pl.addErrors(errors);
					}
					else if (PlotArea.compareTo(PLOT_AREA_100) > 0 && PlotArea.compareTo(PLOT_AREA_250) <= 0) {
						minVal=REARYARDMINIMUM_DISTANCE_1_2;
					} else if (PlotArea.compareTo(PLOT_AREA_250) > 0 && PlotArea.compareTo(PLOT_AREA_500) <= 0) {
						minVal=REARYARDMINIMUM_DISTANCE_1_2;

					} else if (PlotArea.compareTo(PLOT_AREA_500) > 0 && PlotArea.compareTo(PLOT_AREA_1000) <= 0) {
						minVal=REARYARDMINIMUM_DISTANCE_1_2;
					} else if (PlotArea.compareTo(PLOT_AREA_1000) > 0 && PlotArea.compareTo(PLOT_AREA_1500) <= 0) {
						minVal=REARYARDMINIMUM_DISTANCE_1_2;
					} else if (PlotArea.compareTo(PLOT_AREA_1500) > 0 && PlotArea.compareTo(PLOT_AREA_3000) <= 0) {
						minVal=REARYARDMINIMUM_DISTANCE_1_2;
					}else if(PlotArea.compareTo(PLOT_AREA_3000)>0) {
						errors.put("PlotArea Above 3000 hostel","Plot area should not exceed above 3000 sqmts for hostel");
						pl.addErrors(errors);
					}

				}
		valid = validateMinimumValue(min, minVal);
		return compareRearYardResult(block.getName(), min,  mostRestrictiveOccupancyType, rearYardResult, valid, subRule,
				rule, minVal,level);
	}



	private Boolean validateMinimumValue(BigDecimal min, BigDecimal minval) {
		Boolean valid = false;
		if (min.compareTo(minval) >= 0) {
			valid = true;
		}
		return valid;
	}

	private void validateRearYard(final Plan pl) {
		for (Block block : pl.getBlocks()) {
			if (!block.getCompletelyExisting()) {
				Boolean rearYardDefined = false;
				for (SetBack setback : block.getSetBacks()) {
					if (setback.getRearYard() != null
							&& setback.getRearYard().getMean().compareTo(BigDecimal.valueOf(0)) > 0) {
						rearYardDefined = true;
					}
				}
				if (!rearYardDefined) {
					HashMap<String, String> errors = new HashMap<>();
					errors.put(REAR_YARD_DESC,
							prepareMessage(OBJECTNOTDEFINED, REAR_YARD_DESC + " for Block " + block.getName()));
					pl.addErrors(errors);
				}
			}

		}

	}

	private RearYardResult compareRearYardResult(String blockName, BigDecimal min,
			OccupancyTypeHelper mostRestrictiveOccupancy, RearYardResult rearYardResult, Boolean valid, String subRule,
			String rule, BigDecimal minVal, Integer level) {
		String occupancyName;
		if (mostRestrictiveOccupancy.getSubtype() != null)
			occupancyName = mostRestrictiveOccupancy.getSubtype().getName();
		else
			occupancyName = mostRestrictiveOccupancy.getType().getName();
		if (minVal.compareTo(rearYardResult.expectedminimumDistance) >= 0) {
			if (minVal.compareTo(rearYardResult.expectedminimumDistance) == 0) {
				rearYardResult.rule = rearYardResult.rule != null ? rearYardResult.rule + "," + rule : rule;
				rearYardResult.occupancy = rearYardResult.occupancy != null
						? rearYardResult.occupancy + "," + occupancyName
						: occupancyName;

				
			} else {
				rearYardResult.rule = rule;
				rearYardResult.occupancy = occupancyName;

			}

			rearYardResult.subRule = subRule;
			rearYardResult.blockName = blockName;
			rearYardResult.level = level;
			rearYardResult.expectedminimumDistance = minVal;
			rearYardResult.actualMinDistance = min;
			rearYardResult.status = valid;

		}
		return rearYardResult;
	}
}
