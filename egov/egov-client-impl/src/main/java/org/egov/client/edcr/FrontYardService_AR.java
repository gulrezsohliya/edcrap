package org.egov.client.edcr;

import static org.egov.edcr.constants.DxfFileConstants.A;
import static org.egov.edcr.constants.DxfFileConstants.A_AF;
import static org.egov.edcr.constants.DxfFileConstants.A_FH;
import static org.egov.edcr.constants.DxfFileConstants.F_RT;
import static org.egov.edcr.constants.DxfFileConstants.F_CB;

import static org.egov.edcr.constants.DxfFileConstants.A_HE;
import static org.egov.edcr.constants.DxfFileConstants.A_R;
import static org.egov.edcr.constants.DxfFileConstants.F_H;
import static org.egov.edcr.constants.DxfFileConstants.B;
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

	// Arunachal plot area
	private static final BigDecimal PLOT_AREA_60 = BigDecimal.valueOf(60);
	private static final BigDecimal PLOT_AREA_100 = BigDecimal.valueOf(100);
	private static final BigDecimal PLOT_AREA_250 = BigDecimal.valueOf(250);
	private static final BigDecimal PLOT_AREA_500 = BigDecimal.valueOf(500);
	private static final BigDecimal PLOT_AREA_1000 = BigDecimal.valueOf(1000);
	private static final BigDecimal PLOT_AREA_1500 = BigDecimal.valueOf(1500);
	private static final BigDecimal PLOT_AREA_2000 = BigDecimal.valueOf(2000);
	private static final BigDecimal PLOT_AREA_3000 = BigDecimal.valueOf(3000);
	private static final BigDecimal PLOT_AREA_10000 = BigDecimal.valueOf(10000);
	private static final BigDecimal PLOT_AREA_20000 = BigDecimal.valueOf(20000);

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

		LOG.info(" Arunachal Pradesh Set Back Front Yards !!!");
		Plot plot = pl.getPlot();
		HashMap<String, String> errors = new HashMap<>();

		Boolean OCTYP_FOUND = false;
		Boolean OCTYP_NOTFOUND = false;

		if (plot == null)
			return;
		// each blockwise, check height , floor area, buildup area. check most restricve
		// based on occupancy and front yard values
		// of occupancies.
		// If floor area less than 150 mt and occupancy type D, then consider as
		// commercial building.
		// In output show blockwise required and provided information.

		validateFrontYard(pl);

		if (plot != null && !pl.getBlocks().isEmpty()) {
			for (Block block : pl.getBlocks()) { // for each block

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
					BigDecimal min;
					BigDecimal mean;
					// consider height,floor area,buildup area, different occupancies of block
					// Get occupancies of perticular block and use the same.

					if (setback.getFrontYard() != null) {
						min = setback.getFrontYard().getMinimumDistance();
						mean = setback.getFrontYard().getMean();

						LOG.info("Block_" + block.getName());
						LOG.info("FRONTYARD Mean AP : " + mean);
						LOG.info("FRONTYARD Min AP : " + min);

						// if height defined at frontyard level, then use elase use buidling height.
						BigDecimal buildingHeight = setback.getFrontYard().getHeight() != null
								&& setback.getFrontYard().getHeight().compareTo(BigDecimal.ZERO) > 0
										? setback.getFrontYard().getHeight()
										: block.getBuilding().getBuildingHeight();

						LOG.info("Building Height : " + buildingHeight);

						if (buildingHeight == null) {
							errors.put("Block_" + block.getName() + "_" + FRONT_YARD_DESC, FRONT_YARD_DESC
									+ " for Block " + block.getName() + " : unable to get building height !!");
							pl.addErrors(errors);
						} else if (buildingHeight != null && (min.doubleValue() > 0 || mean.doubleValue() > 0)) {
							System.out.println(" size of block.getBuilding().getTotalArea() : "
									+ block.getBuilding().getTotalArea().size());
							System.out.println(" size of block.getBuilding().getMostRestrictiveOccupancy() : "
									+ block.getBuilding().getMostRestrictiveOccupancy());
							System.out.println(" size of block.getBuilding().getOccupancies() : "
									+ block.getBuilding().getOccupancies().size());

							OCTYP_FOUND = false;
							OCTYP_NOTFOUND = false;

							for (final Occupancy occupancy : block.getBuilding().getTotalArea()) {

								scrutinyDetail.setKey("Block_" + block.getName() + "_" + FRONT_YARD_DESC);

//								if (setback.getLevel() < 0) {
//									scrutinyDetail.setKey("Block_" + block.getName() + "_" + "Basement Front Yard");
//									checkFrontYardBasement(pl, block.getBuilding(), block.getName(), setback.getLevel(),
//											plot, BSMT_FRONT_YARD_DESC, min, mean, occupancy.getTypeHelper(),
//											frontYardResult);
//
//								}

								LOG.info("    OCC type  :" + occupancy.getTypeHelper().getType());
//								LOG.info("    OCC type name  :" + occupancy.getTypeHelper().getType().getName());
//								LOG.info("    OCC CODE  :" + occupancy.getTypeHelper().getType().getCode());
//								LOG.info("sub occ type :" + occupancy.getTypeHelper().getSubtype().getName());
//								LOG.info("sub occ Code :" + occupancy.getTypeHelper().getSubtype().getCode());
//								LOG.info("sub occ Color :" + occupancy.getTypeHelper().getSubtype().getColor());
//								LOG.info("sub occ Usage :" + occupancy.getTypeHelper().getUsage().getName());

								if (occupancy.getTypeHelper().getType() == null) {

									OCTYP_NOTFOUND = true; // occ typ not found

//									errors.put("Block_" + block.getName() + "_" + FRONT_YARD_DESC, FRONT_YARD_DESC
//											+ " for Block " + block.getName() + " : Occupancy Type not Found!!");
//									pl.addErrors(errors);
								} else {
									OCTYP_FOUND = true; // search for occ typ
									if ((occupancy.getTypeHelper().getType() != null
											&& A.equalsIgnoreCase(occupancy.getTypeHelper().getType().getCode()))) { // Residential

										frontYardResult = checkFrontYardForResidential(pl, block.getBuilding(),
												block.getName(), setback.getLevel(), plot, FRONT_YARD_DESC, min, mean,
												occupancy.getTypeHelper(), frontYardResult);

									} else if (occupancy.getTypeHelper().getType() != null
											&& F.equalsIgnoreCase(occupancy.getTypeHelper().getType().getCode())) { // Commercial
										frontYardResult = checkFrontYardForCommercial(pl, block.getBuilding(),
												block.getName(), setback.getLevel(), plot, FRONT_YARD_DESC, min, mean,
												occupancy.getTypeHelper(), frontYardResult);
									} else if (occupancy.getTypeHelper().getType() != null
											&& ML.equalsIgnoreCase(occupancy.getTypeHelper().getType().getCode())) { // Mixed
																														// Landuse

										frontYardResult = checkFrontYardForMixedLanduse(pl, block.getBuilding(),
												block.getName(), setback.getLevel(), plot, FRONT_YARD_DESC, min, mean,
												occupancy.getTypeHelper(), frontYardResult);
									}
								}

							}

							if (OCTYP_NOTFOUND == true && OCTYP_FOUND == false) {
								errors.put("Block_" + block.getName() + "_" + FRONT_YARD_DESC, FRONT_YARD_DESC
										+ " for Block " + block.getName() + " : Occupancy Type not Found!!");
								pl.addErrors(errors);
							}

						}

						if (errors.isEmpty()) {
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

	private void validateFrontYard(Plan pl) {

		// Front yard may not be mandatory at each level. We can check whether in any
		// level front yard defined or not ?

		for (Block block : pl.getBlocks()) {
			if (!block.getCompletelyExisting()) {
				Boolean frontYardDefined = false;
				for (SetBack setback : block.getSetBacks()) {
					if (setback.getFrontYard() != null
							&& setback.getFrontYard().getMean().compareTo(BigDecimal.valueOf(0)) > 0) {
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

	private FrontYardResult checkFrontYardForResidential(Plan pl, Building building, String blockName, Integer level,
			Plot plot, String frontYardFieldName, BigDecimal min, BigDecimal mean,
			OccupancyTypeHelper mostRestrictiveOccupancy, FrontYardResult frontYardResult) {
		Boolean valid = false;
		String subRule = RULE_35;
		String rule = FRONT_YARD_DESC;
		BigDecimal minVal = BigDecimal.ZERO;
		BigDecimal meanVal = BigDecimal.ZERO;
		BigDecimal widthOfPlot = pl.getPlanInformation().getWidthOfPlot();

		return processFrontYardForResidential(blockName, level, min, mean, mostRestrictiveOccupancy, frontYardResult,
				valid, subRule, rule, minVal, meanVal, pl.getPlot().getArea(), widthOfPlot);
	}

	private FrontYardResult processFrontYardForResidential(String blockName, Integer level, BigDecimal min,
			BigDecimal mean, OccupancyTypeHelper mostRestrictiveOccupancy, FrontYardResult frontYardResult,
			Boolean valid, String subRule, String rule, BigDecimal minVal, BigDecimal meanVal, BigDecimal plotArea,
			BigDecimal widthOfPlot) {

		LOG.info("PLOTAREA: " + plotArea);

		if (mostRestrictiveOccupancy.getSubtype() != null
				&& A_R.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())) { // Residential

			if (plotArea.compareTo(BigDecimal.valueOf(48)) < 0) {

			} else if (plotArea.compareTo(BigDecimal.valueOf(48)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(60)) <= 0) {
				minVal = FRONTYARDMINIMUM_DISTANCE_2;

			} else if (plotArea.compareTo(BigDecimal.valueOf(60)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(100)) <= 0) {
				minVal = FRONTYARDMINIMUM_DISTANCE_2;

			} else if (plotArea.compareTo(BigDecimal.valueOf(100)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(250)) <= 0) {
				minVal = FRONTYARDMINIMUM_DISTANCE_3;

			} else if (plotArea.compareTo(BigDecimal.valueOf(250)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(500)) <= 0) {
				minVal = FRONTYARDMINIMUM_DISTANCE_3;

			} else if (plotArea.compareTo(BigDecimal.valueOf(500)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(1000)) <= 0) {
				minVal = FRONTYARDMINIMUM_DISTANCE_3;

			} else if (plotArea.compareTo(BigDecimal.valueOf(1000)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(1500)) <= 0) {
				minVal = FRONTYARDMINIMUM_DISTANCE_3;

			} else if (plotArea.compareTo(BigDecimal.valueOf(1500)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(3000)) <= 0) {
				minVal = FRONTYARDMINIMUM_DISTANCE_3;

			} else if (plotArea.compareTo(BigDecimal.valueOf(3000)) > 0) {

			}
		} else if (A_AF.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())) { // Apartment

			if (plotArea.compareTo(BigDecimal.valueOf(48)) < 0) {

			} else if (plotArea.compareTo(BigDecimal.valueOf(48)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(60)) <= 0) {
				minVal = FRONTYARDMINIMUM_DISTANCE_2;

			} else if (plotArea.compareTo(BigDecimal.valueOf(60)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(100)) <= 0) {
				minVal = FRONTYARDMINIMUM_DISTANCE_2;

			} else if (plotArea.compareTo(BigDecimal.valueOf(100)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(250)) <= 0) {
				minVal = FRONTYARDMINIMUM_DISTANCE_3;

			} else if (plotArea.compareTo(BigDecimal.valueOf(250)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(500)) <= 0) {
				minVal = FRONTYARDMINIMUM_DISTANCE_3;

			} else if (plotArea.compareTo(BigDecimal.valueOf(500)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(1000)) <= 0) {
				minVal = FRONTYARDMINIMUM_DISTANCE_3;

			} else if (plotArea.compareTo(BigDecimal.valueOf(1000)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(1500)) <= 0) {
				minVal = FRONTYARDMINIMUM_DISTANCE_3;

			} else if (plotArea.compareTo(BigDecimal.valueOf(1500)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(3000)) <= 0) {
				minVal = FRONTYARDMINIMUM_DISTANCE_3;

			} else if (plotArea.compareTo(BigDecimal.valueOf(3000)) > 0) {

			}

		} else if (A_RH.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())) { // Row Housing

			minVal = FRONTYARDMINIMUM_DISTANCE_2;

		} else if (A_HE.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())
				|| A_BH.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())
				|| A_LH.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())
				|| A_GH.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())) { // Hostel/ Boarding
																								// House/Lodging House/
																								// Guest House

			if (plotArea.compareTo(PLOT_AREA_100) > 0 && plotArea.compareTo(PLOT_AREA_250) <= 0) {

				minVal = FRONTYARDMINIMUM_DISTANCE_3;

			} else if (plotArea.compareTo(PLOT_AREA_250) > 0 && plotArea.compareTo(PLOT_AREA_500) <= 0) {

				minVal = FRONTYARDMINIMUM_DISTANCE_3;

			} else if (plotArea.compareTo(BigDecimal.valueOf(500)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(1000)) <= 0) {
				minVal = FRONTYARDMINIMUM_DISTANCE_3;

			} else if (plotArea.compareTo(BigDecimal.valueOf(1000)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(1500)) <= 0) {
				minVal = FRONTYARDMINIMUM_DISTANCE_3;

			} else if (plotArea.compareTo(BigDecimal.valueOf(1500)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(3000)) <= 0) {
				minVal = FRONTYARDMINIMUM_DISTANCE_3;

			}
		}

		LOG.info("min : " + min);
		LOG.info("mean : " + mean);
		LOG.info("minVal : " + minVal);
		LOG.info("meanVal : " + meanVal);

		/*
		 * if (-1 == level) { rule = BSMT_FRONT_YARD_DESC; subRuleDesc =
		 * SUB_RULE_24_12_DESCRIPTION; subRule = SUB_RULE_24_12; }
		 */

//		valid = validateMinimumAndMeanValue(min, mean, minVal, meanVal);
		valid = validateMinimumValue(min, minVal);

		return compareFrontYardResult(blockName, min, mean, mostRestrictiveOccupancy, frontYardResult, valid, subRule,
				rule, minVal, meanVal, level);
	}

	private FrontYardResult checkFrontYardForCommercial(Plan pl, Building building, String blockName, Integer level,
			Plot plot, String frontYardFieldName, BigDecimal min, BigDecimal mean,
			OccupancyTypeHelper mostRestrictiveOccupancy, FrontYardResult frontYardResult) {
		Boolean valid = false;
		String subRule = RULE_35;
		String rule = FRONT_YARD_DESC;
		BigDecimal minVal = BigDecimal.ZERO;
		BigDecimal meanVal = BigDecimal.ZERO;
		BigDecimal widthOfPlot = pl.getPlanInformation().getWidthOfPlot();

		return processFrontYardForCommercial(blockName, level, min, mean, mostRestrictiveOccupancy, frontYardResult,
				valid, subRule, rule, minVal, meanVal, pl.getPlot().getArea(), widthOfPlot);
	}

	private FrontYardResult processFrontYardForCommercial(String blockName, Integer level, BigDecimal min,
			BigDecimal mean, OccupancyTypeHelper mostRestrictiveOccupancy, FrontYardResult frontYardResult,
			Boolean valid, String subRule, String rule, BigDecimal minVal, BigDecimal meanVal, BigDecimal plotArea,
			BigDecimal widthOfPlot) {
		if (mostRestrictiveOccupancy.getSubtype() != null
				&& (F_RT.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())
						|| F_SH.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())
						|| F_CB.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode()))) { // Shops/Restaurant/Commercial

			if (plotArea.compareTo(BigDecimal.valueOf(48)) < 0) {

			} else if (plotArea.compareTo(BigDecimal.valueOf(48)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(100)) <= 0) {
				minVal = FRONTYARDMINIMUM_DISTANCE_2;

			} else if (plotArea.compareTo(PLOT_AREA_100) > 0 && plotArea.compareTo(PLOT_AREA_250) <= 0) {

				minVal = FRONTYARDMINIMUM_DISTANCE_3;

			} else if (plotArea.compareTo(PLOT_AREA_250) > 0 && plotArea.compareTo(PLOT_AREA_500) <= 0) {

				minVal = FRONTYARDMINIMUM_DISTANCE_3;

			} else if (plotArea.compareTo(BigDecimal.valueOf(500)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(1000)) <= 0) {
				minVal = FRONTYARDMINIMUM_DISTANCE_3;

			} else if (plotArea.compareTo(BigDecimal.valueOf(1000)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(1500)) <= 0) {
				minVal = FRONTYARDMINIMUM_DISTANCE_3;

			} else if (plotArea.compareTo(BigDecimal.valueOf(1500)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(3000)) <= 0) {
				minVal = FRONTYARDMINIMUM_DISTANCE_3;

			}

		} else if (F_PTP.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())) { // Petrol Pump

			if (mostRestrictiveOccupancy.getUsage() != null) {
				if (F_PTP_FCSS.equalsIgnoreCase(mostRestrictiveOccupancy.getUsage().getCode())) { // Filling-cum-service
																									// station

					if (plotArea.compareTo(BigDecimal.valueOf(1080)) > 0) {
						minVal = FRONTYARDMINIMUM_DISTANCE_30;
					}

				} else if (F_PTP_FS.equalsIgnoreCase(mostRestrictiveOccupancy.getUsage().getCode())) { // Filling
																										// station

					if (plotArea.compareTo(BigDecimal.valueOf(510)) > 0) {
						minVal = FRONTYARDMINIMUM_DISTANCE_30;
					}
				}
			}

		} else if (F_WH.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())) {

		}

		/*
		 * if (-1 == level) { rule = BSMT_FRONT_YARD_DESC; subRuleDesc =
		 * SUB_RULE_24_12_DESCRIPTION; subRule = SUB_RULE_24_12; }
		 */

//		valid = validateMinimumAndMeanValue(min, mean, minVal, meanVal);
		valid = validateMinimumValue(min, minVal);

		return compareFrontYardResult(blockName, min, mean, mostRestrictiveOccupancy, frontYardResult, valid, subRule,
				rule, minVal, meanVal, level);
	}

	private FrontYardResult checkFrontYardForMixedLanduse(Plan pl, Building building, String blockName, Integer level,
			Plot plot, String frontYardFieldName, BigDecimal min, BigDecimal mean,
			OccupancyTypeHelper mostRestrictiveOccupancy, FrontYardResult frontYardResult) {
		Boolean valid = false;
		String subRule = RULE_35;
		String rule = FRONT_YARD_DESC;
		BigDecimal minVal = BigDecimal.ZERO;
		BigDecimal meanVal = BigDecimal.ZERO;
		BigDecimal widthOfPlot = pl.getPlanInformation().getWidthOfPlot();

		return processFrontYardForMixedLanduse(blockName, level, min, mean, mostRestrictiveOccupancy, frontYardResult,
				valid, subRule, rule, minVal, meanVal, pl.getPlot().getArea(), widthOfPlot);
	}

	private FrontYardResult processFrontYardForMixedLanduse(String blockName, Integer level, BigDecimal min,
			BigDecimal mean, OccupancyTypeHelper mostRestrictiveOccupancy, FrontYardResult frontYardResult,
			Boolean valid, String subRule, String rule, BigDecimal minVal, BigDecimal meanVal, BigDecimal plotArea,
			BigDecimal widthOfPlot) {

		LOG.info("PLOTAREA: " + plotArea);

		if (mostRestrictiveOccupancy.getSubtype() != null
				&& ML_A_R.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())) { // Mixed Land Residential

			if (plotArea.compareTo(BigDecimal.valueOf(48)) < 0) {

			} else if (plotArea.compareTo(BigDecimal.valueOf(48)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(60)) <= 0) {
				minVal = FRONTYARDMINIMUM_DISTANCE_2;

			} else if (plotArea.compareTo(BigDecimal.valueOf(60)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(100)) <= 0) {
				minVal = FRONTYARDMINIMUM_DISTANCE_2;

			} else if (plotArea.compareTo(BigDecimal.valueOf(100)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(250)) <= 0) {
				minVal = FRONTYARDMINIMUM_DISTANCE_3;

			} else if (plotArea.compareTo(BigDecimal.valueOf(250)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(500)) <= 0) {
				minVal = FRONTYARDMINIMUM_DISTANCE_3;

			} else if (plotArea.compareTo(BigDecimal.valueOf(500)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(1000)) <= 0) {
				minVal = FRONTYARDMINIMUM_DISTANCE_3;

			} else if (plotArea.compareTo(BigDecimal.valueOf(1000)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(1500)) <= 0) {
				minVal = FRONTYARDMINIMUM_DISTANCE_3;

			} else if (plotArea.compareTo(BigDecimal.valueOf(1500)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(3000)) <= 0) {
				minVal = FRONTYARDMINIMUM_DISTANCE_3;

			} else if (plotArea.compareTo(BigDecimal.valueOf(3000)) > 0) {

			}
		} else if (ML_A_AF.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())) { // Apartment

			if (plotArea.compareTo(BigDecimal.valueOf(48)) < 0) {

			} else if (plotArea.compareTo(BigDecimal.valueOf(48)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(60)) <= 0) {
				minVal = FRONTYARDMINIMUM_DISTANCE_2;

			} else if (plotArea.compareTo(BigDecimal.valueOf(60)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(100)) <= 0) {
				minVal = FRONTYARDMINIMUM_DISTANCE_2;

			} else if (plotArea.compareTo(BigDecimal.valueOf(100)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(250)) <= 0) {
				minVal = FRONTYARDMINIMUM_DISTANCE_3;

			} else if (plotArea.compareTo(BigDecimal.valueOf(250)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(500)) <= 0) {
				minVal = FRONTYARDMINIMUM_DISTANCE_3;

			} else if (plotArea.compareTo(BigDecimal.valueOf(500)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(1000)) <= 0) {
				minVal = FRONTYARDMINIMUM_DISTANCE_3;

			} else if (plotArea.compareTo(BigDecimal.valueOf(1000)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(1500)) <= 0) {
				minVal = FRONTYARDMINIMUM_DISTANCE_3;

			} else if (plotArea.compareTo(BigDecimal.valueOf(1500)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(3000)) <= 0) {
				minVal = FRONTYARDMINIMUM_DISTANCE_3;

			} else if (plotArea.compareTo(BigDecimal.valueOf(3000)) > 0) {

			}

		} else if (ML_A_RH.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())) { // Row Housing

			minVal = FRONTYARDMINIMUM_DISTANCE_2;

		} else if (ML_A_HE.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())
				|| ML_A_BH.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())) { // Hostel/ Boarding
																								// House

			if (plotArea.compareTo(BigDecimal.valueOf(500)) < 0) {
				minVal = FRONTYARDMINIMUM_DISTANCE_3;

			} else if (plotArea.compareTo(BigDecimal.valueOf(500)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(1000)) <= 0) {
				minVal = FRONTYARDMINIMUM_DISTANCE_3;

			} else if (plotArea.compareTo(BigDecimal.valueOf(1000)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(2000)) <= 0) {
				minVal = FRONTYARDMINIMUM_DISTANCE_3;

			} else if (plotArea.compareTo(BigDecimal.valueOf(2000)) > 0) {
				minVal = FRONTYARDMINIMUM_DISTANCE_3;

			}
		}

		if (mostRestrictiveOccupancy.getSubtype() != null
				&& ML_F_RT.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())) { // Mixed Land Commercial

			if (plotArea.compareTo(BigDecimal.valueOf(48)) < 0) {

			} else if (plotArea.compareTo(BigDecimal.valueOf(48)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(100)) <= 0) {
				minVal = FRONTYARDMINIMUM_DISTANCE_2;

			} else if (plotArea.compareTo(BigDecimal.valueOf(100)) > 0) {
				minVal = FRONTYARDMINIMUM_DISTANCE_3;

			}

		} else if (ML_F_PTP.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())) { // Petrol Pump

			if (mostRestrictiveOccupancy.getUsage() != null) {
				if (ML_F_PTP_FCSS.equalsIgnoreCase(mostRestrictiveOccupancy.getUsage().getCode())) { // Filling-cum-service
																										// station

					if (plotArea.compareTo(BigDecimal.valueOf(1080)) > 0) {
						minVal = FRONTYARDMINIMUM_DISTANCE_30;
					}

				} else if (ML_F_PTP_FS.equalsIgnoreCase(mostRestrictiveOccupancy.getUsage().getCode())) { // Filling
																											// station

					if (plotArea.compareTo(BigDecimal.valueOf(510)) > 0) {
						minVal = FRONTYARDMINIMUM_DISTANCE_30;
					}
				}
			}

		}

		LOG.info("min : " + min);
		LOG.info("mean : " + mean);
		LOG.info("minVal : " + minVal);
		LOG.info("meanVal : " + meanVal);

		/*
		 * if (-1 == level) { rule = BSMT_FRONT_YARD_DESC; subRuleDesc =
		 * SUB_RULE_24_12_DESCRIPTION; subRule = SUB_RULE_24_12; }
		 */

//		valid = validateMinimumAndMeanValue(min, mean, minVal, meanVal);
		valid = validateMinimumValue(min, minVal);

		return compareFrontYardResult(blockName, min, mean, mostRestrictiveOccupancy, frontYardResult, valid, subRule,
				rule, minVal, meanVal, level);
	}

	private FrontYardResult compareFrontYardResult(String blockName, BigDecimal min, BigDecimal mean,
			OccupancyTypeHelper mostRestrictiveOccupancy, FrontYardResult frontYardResult, Boolean valid,
			String subRule, String rule, BigDecimal minVal, BigDecimal meanVal, Integer level) {
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
			frontYardResult.expectedmeanDistance = meanVal;
			frontYardResult.actualMinDistance = min;
			frontYardResult.actualMeanDistance = mean;
			frontYardResult.status = valid;

		}
		return frontYardResult;
	}

	private Boolean validateMinimumAndMeanValue(BigDecimal min, BigDecimal mean, BigDecimal minval,
			BigDecimal meanval) {
		Boolean valid = false;
		if (min.compareTo(minval) >= 0 && mean.compareTo(meanval) >= 0) {
			valid = true;
		}
		return valid;
	}

	private Boolean validateMinimumValue(BigDecimal min, BigDecimal minval) {
		Boolean valid = false;
		if (min.compareTo(minval) >= 0) {
			valid = true;
		}
		return valid;
	}
}
