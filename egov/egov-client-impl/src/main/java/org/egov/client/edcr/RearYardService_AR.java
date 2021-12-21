package org.egov.client.edcr;

import static org.egov.edcr.constants.DxfFileConstants.A;
import static org.egov.edcr.constants.DxfFileConstants.A_AF;
import static org.egov.edcr.constants.DxfFileConstants.A_FH;
import static org.egov.edcr.constants.DxfFileConstants.F_RT;
import static org.egov.edcr.constants.DxfFileConstants.A_HE;
import static org.egov.edcr.constants.DxfFileConstants.A_R;
import static org.egov.edcr.constants.DxfFileConstants.B;
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
		LOG.info(" Arunachal Pradesh Set Back REAR Yards !!!");

		HashMap<String, String> errors = new HashMap<>();
		final Plot plot = pl.getPlot();

		Boolean OCTYP_FOUND = false;
		Boolean OCTYP_NOTFOUND = false;

		if (plot == null)
			return;

		validateRearYard(pl);

		if (plot != null && !pl.getBlocks().isEmpty()) {
			for (Block block : pl.getBlocks()) { // for each block

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
					BigDecimal min;
					BigDecimal mean;

					if (setback.getRearYard() != null
							&& setback.getRearYard().getMean().compareTo(BigDecimal.ZERO) > 0) {
						min = setback.getRearYard().getMinimumDistance();
						mean = setback.getRearYard().getMean();

						LOG.info("Block_" + block.getName());
						LOG.info("REARYARD Mean AP" + mean);
						LOG.info("REARYARD Min AP" + min);

						// if height defined at rear yard level, then use elase use buidling height.
						BigDecimal buildingHeight = setback.getRearYard().getHeight() != null
								&& setback.getRearYard().getHeight().compareTo(BigDecimal.ZERO) > 0
										? setback.getRearYard().getHeight()
										: block.getBuilding().getBuildingHeight();

						if (buildingHeight == null) {
							errors.put("Block_" + block.getName() + "_" + REAR_YARD_DESC, REAR_YARD_DESC + " for Block "
									+ block.getName() + " : unable to get building height !!");
							pl.addErrors(errors);
						} else if (buildingHeight != null && (min.doubleValue() > 0 || mean.doubleValue() > 0)) {
							OCTYP_FOUND = false;
							OCTYP_NOTFOUND = false;

							for (final Occupancy occupancy : block.getBuilding().getTotalArea()) {
								scrutinyDetail.setKey("Block_" + block.getName() + "_" + "Rear Setback");

//								if (setback.getLevel() < 0) {
//									scrutinyDetail.setKey("Block_" + block.getName() + "_" + "Basement Rear Setback");
//									checkRearYardBasement(pl, block.getBuilding(), block.getName(), setback.getLevel(),
//											plot, BSMT_REAR_YARD_DESC, min, mean, occupancy.getTypeHelper(),
//											rearYardResult);
//
//								}

//								LOG.info("    OCC type  :" + occupancy.getTypeHelper().getType().getName());
//								LOG.info("    OCC CODE  :" + occupancy.getTypeHelper().getType().getCode());
//								LOG.info("sub occ type :" + occupancy.getTypeHelper().getSubtype().getName());
//								LOG.info("sub occ Code :" + occupancy.getTypeHelper().getSubtype().getCode());
//								LOG.info("sub occ Color :" + occupancy.getTypeHelper().getSubtype().getColor());
//								LOG.info("sub occ Usage :" + occupancy.getTypeHelper().getUsage().getName());
								if (occupancy.getTypeHelper().getType() == null) {

									OCTYP_NOTFOUND = true; // occ typ not found
//									errors.put("Block_" + block.getName() + "_" + REAR_YARD_DESC, REAR_YARD_DESC
//											+ " for Block " + block.getName() + " : Occupancy Type not Found!!");
//									pl.addErrors(errors);
								} else {

									OCTYP_FOUND = true; // search for occ typ
									if ((occupancy.getTypeHelper().getType() != null
											&& (A.equalsIgnoreCase(occupancy.getTypeHelper().getType().getCode())))) { // Residential

										rearYardResult = checkRearYardForResidential(setback, block.getBuilding(), pl,
												block, setback.getLevel(), plot, REAR_YARD_DESC, min, mean,
												occupancy.getTypeHelper(), rearYardResult);

									} else if (F.equalsIgnoreCase(occupancy.getTypeHelper().getType().getCode())) { // Commercial
										rearYardResult = checkRearYardForCommercial(setback, block.getBuilding(), pl,
												block, setback.getLevel(), plot, REAR_YARD_DESC, min, mean,
												occupancy.getTypeHelper(), rearYardResult);
									} else if (ML.equalsIgnoreCase(occupancy.getTypeHelper().getType().getCode())) { // Mixed
																														// Landuse
										rearYardResult = checkRearYardForMixedLanduse(setback, block.getBuilding(), pl,
												block, setback.getLevel(), plot, REAR_YARD_DESC, min, mean,
												occupancy.getTypeHelper(), rearYardResult);
									}
								}

							}
							if (OCTYP_NOTFOUND == true && OCTYP_FOUND == false) {
								errors.put("Block_" + block.getName() + "_" + REAR_YARD_DESC, REAR_YARD_DESC
										+ " for Block " + block.getName() + " : Occupancy Type not Found!!");
								pl.addErrors(errors);
							}

							if (errors.isEmpty()) {
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

	private RearYardResult checkRearYardForResidential(SetBack setback, Building building, final Plan pl, Block block,
			Integer level, final Plot plot, final String rearYardFieldName, final BigDecimal min, final BigDecimal mean,
			final OccupancyTypeHelper mostRestrictiveOccupancy, RearYardResult rearYardResult) {
		String subRule = RULE_35;
		String rule = REAR_YARD_DESC;
		Boolean valid = false;
		BigDecimal minVal = BigDecimal.valueOf(0);
		BigDecimal meanVal = BigDecimal.valueOf(0);
		BigDecimal widthOfPlot = pl.getPlanInformation().getWidthOfPlot();

		return processRearYardForResidential(block, level, min, mean, mostRestrictiveOccupancy, rearYardResult, subRule,
				rule, minVal, meanVal, pl.getPlot().getArea(), widthOfPlot, valid);
	}

	private RearYardResult processRearYardForResidential(Block block, Integer level, final BigDecimal min,
			final BigDecimal mean, final OccupancyTypeHelper mostRestrictiveOccupancy, RearYardResult rearYardResult,
			String subRule, String rule, BigDecimal minVal, BigDecimal meanVal, BigDecimal plotArea,
			BigDecimal widthOfPlot, Boolean valid) {

		LOG.info("PLOT AREA : " + plotArea);

		if (mostRestrictiveOccupancy.getSubtype() != null
				&& A_R.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())) { // Residential

			if (plotArea.compareTo(BigDecimal.valueOf(48)) < 0) {

			} else if (plotArea.compareTo(BigDecimal.valueOf(48)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(60)) <= 0) {
				minVal = REARYARDMINIMUM_DISTANCE_1;

			} else if (plotArea.compareTo(BigDecimal.valueOf(60)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(100)) <= 0) {
				minVal = REARYARDMINIMUM_DISTANCE_1;

			} else if (plotArea.compareTo(BigDecimal.valueOf(100)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(250)) <= 0) {
				minVal = REARYARDMINIMUM_DISTANCE_1_2;

			} else if (plotArea.compareTo(BigDecimal.valueOf(250)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(500)) <= 0) {
				minVal = REARYARDMINIMUM_DISTANCE_1_2;

			} else if (plotArea.compareTo(BigDecimal.valueOf(500)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(1000)) <= 0) {
				minVal = REARYARDMINIMUM_DISTANCE_1_2;

			} else if (plotArea.compareTo(BigDecimal.valueOf(1000)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(1500)) <= 0) {
				minVal = REARYARDMINIMUM_DISTANCE_1_2;

			} else if (plotArea.compareTo(BigDecimal.valueOf(1500)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(3000)) <= 0) {
				minVal = REARYARDMINIMUM_DISTANCE_1_2;

			} else if (plotArea.compareTo(BigDecimal.valueOf(3000)) > 0) {

			}
		} else if (A_AF.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())) { // Apartment
			if (plotArea.compareTo(BigDecimal.valueOf(48)) < 0) {

			} else if (plotArea.compareTo(BigDecimal.valueOf(48)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(60)) <= 0) {
				minVal = REARYARDMINIMUM_DISTANCE_1;

			} else if (plotArea.compareTo(BigDecimal.valueOf(60)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(100)) <= 0) {
				minVal = REARYARDMINIMUM_DISTANCE_1;

			} else if (plotArea.compareTo(BigDecimal.valueOf(100)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(250)) <= 0) {
				minVal = REARYARDMINIMUM_DISTANCE_1_2;

			} else if (plotArea.compareTo(BigDecimal.valueOf(250)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(500)) <= 0) {
				minVal = REARYARDMINIMUM_DISTANCE_1_2;

			} else if (plotArea.compareTo(BigDecimal.valueOf(500)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(1000)) <= 0) {
				minVal = REARYARDMINIMUM_DISTANCE_1_2;

			} else if (plotArea.compareTo(BigDecimal.valueOf(1000)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(1500)) <= 0) {
				minVal = REARYARDMINIMUM_DISTANCE_1_2;

			} else if (plotArea.compareTo(BigDecimal.valueOf(1500)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(3000)) <= 0) {
				minVal = REARYARDMINIMUM_DISTANCE_1_2;

			} else if (plotArea.compareTo(BigDecimal.valueOf(3000)) > 0) {

			}
		} else if (A_RH.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())) { // Row Housing

			minVal = REARYARDMINIMUM_DISTANCE_1;

		} else if (A_HE.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())
				|| A_BH.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())
				|| A_LH.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())
				|| A_GH.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())) { // Hostel/ Boarding
																								// House/Lodging House/
																								// Guest House

			if (plotArea.compareTo(PLOT_AREA_100) > 0 && plotArea.compareTo(PLOT_AREA_250) <= 0) {

				minVal = REARYARDMINIMUM_DISTANCE_1_2;

			} else if (plotArea.compareTo(PLOT_AREA_250) > 0 && plotArea.compareTo(PLOT_AREA_500) <= 0) {
				minVal = REARYARDMINIMUM_DISTANCE_1_2;

			} else if (plotArea.compareTo(BigDecimal.valueOf(500)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(1000)) <= 0) {
				minVal = REARYARDMINIMUM_DISTANCE_1_2;

			} else if (plotArea.compareTo(BigDecimal.valueOf(1000)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(1500)) <= 0) {
				minVal = REARYARDMINIMUM_DISTANCE_1_2;

			} else if (plotArea.compareTo(BigDecimal.valueOf(1500)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(3000)) <= 0) {
				minVal = REARYARDMINIMUM_DISTANCE_1_2;

			}
		}

		LOG.info("min : " + min);
		LOG.info("mean : " + mean);
		LOG.info("minVal : " + minVal);
		LOG.info("meanVal : " + meanVal);
//		valid = validateMinimumAndMeanValue(min, mean, minVal, meanVal);
		valid = validateMinimumValue(min, minVal);
		/*
		 * if (-1 == level) { subRule = SUB_RULE_24_12; rule = BSMT_REAR_YARD_DESC;
		 * subRuleDesc = SUB_RULE_24_12_DESCRIPTION; }
		 */

		return compareRearYardResult(block.getName(), min, mean, mostRestrictiveOccupancy, rearYardResult, valid,
				subRule, rule, minVal, meanVal, level);
	}

	private RearYardResult checkRearYardForCommercial(SetBack setback, Building building, final Plan pl, Block block,
			Integer level, final Plot plot, final String rearYardFieldName, final BigDecimal min, final BigDecimal mean,
			final OccupancyTypeHelper mostRestrictiveOccupancy, RearYardResult rearYardResult) {
		String subRule = RULE_35;
		String rule = REAR_YARD_DESC;
		Boolean valid = false;
		BigDecimal minVal = BigDecimal.valueOf(0);
		BigDecimal meanVal = BigDecimal.valueOf(0);
		BigDecimal widthOfPlot = pl.getPlanInformation().getWidthOfPlot();

		return processRearYardForCommercial(block, level, min, mean, mostRestrictiveOccupancy, rearYardResult, subRule,
				rule, minVal, meanVal, pl.getPlot().getArea(), widthOfPlot, valid);
	}

	private RearYardResult processRearYardForCommercial(Block block, Integer level, final BigDecimal min,
			final BigDecimal mean, final OccupancyTypeHelper mostRestrictiveOccupancy, RearYardResult rearYardResult,
			String subRule, String rule, BigDecimal minVal, BigDecimal meanVal, BigDecimal plotArea,
			BigDecimal widthOfPlot, Boolean valid) {

		if (mostRestrictiveOccupancy.getSubtype() != null
				&& (F_RT.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())
						|| F_SH.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())
						|| F_CB.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode()))) { // Shops/Restaurant/Commercial

			if (plotArea.compareTo(BigDecimal.valueOf(48)) < 0) {

			} else if (plotArea.compareTo(BigDecimal.valueOf(48)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(100)) <= 0) {
				minVal = REARYARDMINIMUM_DISTANCE_1_2;

			} else if (plotArea.compareTo(PLOT_AREA_100) > 0 && plotArea.compareTo(PLOT_AREA_250) <= 0) {

				minVal = REARYARDMINIMUM_DISTANCE_1_2;

			} else if (plotArea.compareTo(PLOT_AREA_250) > 0 && plotArea.compareTo(PLOT_AREA_500) <= 0) {
				minVal = REARYARDMINIMUM_DISTANCE_1_2;

			} else if (plotArea.compareTo(BigDecimal.valueOf(500)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(1000)) <= 0) {
				minVal = REARYARDMINIMUM_DISTANCE_1_2;

			} else if (plotArea.compareTo(BigDecimal.valueOf(1000)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(1500)) <= 0) {
				minVal = REARYARDMINIMUM_DISTANCE_1_2;

			} else if (plotArea.compareTo(BigDecimal.valueOf(1500)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(3000)) <= 0) {
				minVal = REARYARDMINIMUM_DISTANCE_1_2;

			}

		}

//		valid = validateMinimumAndMeanValue(min, mean, minVal, meanVal);
		valid = validateMinimumValue(min, minVal);
		/*
		 * if (-1 == level) { subRule = SUB_RULE_24_12; rule = BSMT_REAR_YARD_DESC;
		 * subRuleDesc = SUB_RULE_24_12_DESCRIPTION; }
		 */

		return compareRearYardResult(block.getName(), min, mean, mostRestrictiveOccupancy, rearYardResult, valid,
				subRule, rule, minVal, meanVal, level);
	}

	private RearYardResult checkRearYardForMixedLanduse(SetBack setback, Building building, final Plan pl, Block block,
			Integer level, final Plot plot, final String rearYardFieldName, final BigDecimal min, final BigDecimal mean,
			final OccupancyTypeHelper mostRestrictiveOccupancy, RearYardResult rearYardResult) {
		String subRule = RULE_35;
		String rule = REAR_YARD_DESC;
		Boolean valid = false;
		BigDecimal minVal = BigDecimal.valueOf(0);
		BigDecimal meanVal = BigDecimal.valueOf(0);
		BigDecimal widthOfPlot = pl.getPlanInformation().getWidthOfPlot();

		return processRearYardForMixedLanduse(block, level, min, mean, mostRestrictiveOccupancy, rearYardResult,
				subRule, rule, minVal, meanVal, pl.getPlot().getArea(), widthOfPlot, valid);
	}

	private RearYardResult processRearYardForMixedLanduse(Block block, Integer level, final BigDecimal min,
			final BigDecimal mean, final OccupancyTypeHelper mostRestrictiveOccupancy, RearYardResult rearYardResult,
			String subRule, String rule, BigDecimal minVal, BigDecimal meanVal, BigDecimal plotArea,
			BigDecimal widthOfPlot, Boolean valid) {

		LOG.info("PLOT AREA : " + plotArea);

		if (mostRestrictiveOccupancy.getSubtype() != null
				&& ML_A_R.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())) { // Residential

			if (plotArea.compareTo(BigDecimal.valueOf(48)) < 0) {

			} else if (plotArea.compareTo(BigDecimal.valueOf(48)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(60)) <= 0) {
				minVal = REARYARDMINIMUM_DISTANCE_1;

			} else if (plotArea.compareTo(BigDecimal.valueOf(60)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(100)) <= 0) {
				minVal = REARYARDMINIMUM_DISTANCE_1;

			} else if (plotArea.compareTo(BigDecimal.valueOf(100)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(250)) <= 0) {
				minVal = REARYARDMINIMUM_DISTANCE_1_2;

			} else if (plotArea.compareTo(BigDecimal.valueOf(250)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(500)) <= 0) {
				minVal = REARYARDMINIMUM_DISTANCE_1_2;

			} else if (plotArea.compareTo(BigDecimal.valueOf(500)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(1000)) <= 0) {
				minVal = REARYARDMINIMUM_DISTANCE_1_2;

			} else if (plotArea.compareTo(BigDecimal.valueOf(1000)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(1500)) <= 0) {
				minVal = REARYARDMINIMUM_DISTANCE_1_2;

			} else if (plotArea.compareTo(BigDecimal.valueOf(1500)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(3000)) <= 0) {
				minVal = REARYARDMINIMUM_DISTANCE_1_2;

			} else if (plotArea.compareTo(BigDecimal.valueOf(3000)) > 0) {

			}
		} else if (ML_A_AF.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())) { // Apartment
			if (plotArea.compareTo(BigDecimal.valueOf(48)) < 0) {

			} else if (plotArea.compareTo(BigDecimal.valueOf(48)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(60)) <= 0) {
				minVal = REARYARDMINIMUM_DISTANCE_1;

			} else if (plotArea.compareTo(BigDecimal.valueOf(60)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(100)) <= 0) {
				minVal = REARYARDMINIMUM_DISTANCE_1;

			} else if (plotArea.compareTo(BigDecimal.valueOf(100)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(250)) <= 0) {
				minVal = REARYARDMINIMUM_DISTANCE_1_2;

			} else if (plotArea.compareTo(BigDecimal.valueOf(250)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(500)) <= 0) {
				minVal = REARYARDMINIMUM_DISTANCE_1_2;

			} else if (plotArea.compareTo(BigDecimal.valueOf(500)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(1000)) <= 0) {
				minVal = REARYARDMINIMUM_DISTANCE_1_2;

			} else if (plotArea.compareTo(BigDecimal.valueOf(1000)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(1500)) <= 0) {
				minVal = REARYARDMINIMUM_DISTANCE_1_2;

			} else if (plotArea.compareTo(BigDecimal.valueOf(1500)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(3000)) <= 0) {
				minVal = REARYARDMINIMUM_DISTANCE_1_2;

			} else if (plotArea.compareTo(BigDecimal.valueOf(3000)) > 0) {

			}
		} else if (ML_A_RH.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())) { // Row Housing

			minVal = REARYARDMINIMUM_DISTANCE_1;

		} else if (ML_A_HE.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())
				|| ML_A_BH.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())) { // Hostel/ Boarding
																								// House

			if (plotArea.compareTo(BigDecimal.valueOf(500)) < 0) {
				minVal = REARYARDMINIMUM_DISTANCE_1_2;

			} else if (plotArea.compareTo(BigDecimal.valueOf(500)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(1000)) <= 0) {
				minVal = REARYARDMINIMUM_DISTANCE_1_2;

			} else if (plotArea.compareTo(BigDecimal.valueOf(1000)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(2000)) <= 0) {
				minVal = REARYARDMINIMUM_DISTANCE_1_2;

			} else if (plotArea.compareTo(BigDecimal.valueOf(2000)) > 0) {
				minVal = REARYARDMINIMUM_DISTANCE_1_2;

			}
		}

		if (mostRestrictiveOccupancy.getSubtype() != null
				&& (ML_F_RT.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())
						|| ML_F_SH.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode()))) {

			if (plotArea.compareTo(BigDecimal.valueOf(48)) < 0) {

			} else if (plotArea.compareTo(BigDecimal.valueOf(48)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(100)) <= 0) {
				minVal = REARYARDMINIMUM_DISTANCE_1;

			} else if (plotArea.compareTo(BigDecimal.valueOf(100)) > 0) {
				minVal = REARYARDMINIMUM_DISTANCE_1_2;

			}

		}

		LOG.info("min : " + min);
		LOG.info("mean : " + mean);
		LOG.info("minVal : " + minVal);
		LOG.info("meanVal : " + meanVal);
//		valid = validateMinimumAndMeanValue(min, mean, minVal, meanVal);
		valid = validateMinimumValue(min, minVal);
		/*
		 * if (-1 == level) { subRule = SUB_RULE_24_12; rule = BSMT_REAR_YARD_DESC;
		 * subRuleDesc = SUB_RULE_24_12_DESCRIPTION; }
		 */

		return compareRearYardResult(block.getName(), min, mean, mostRestrictiveOccupancy, rearYardResult, valid,
				subRule, rule, minVal, meanVal, level);
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
				if (!rearYardDefined && !pl.getPlanInformation().getNocToAbutRearDesc().equalsIgnoreCase(YES)) {
					HashMap<String, String> errors = new HashMap<>();
					errors.put(REAR_YARD_DESC,
							prepareMessage(OBJECTNOTDEFINED, REAR_YARD_DESC + " for Block " + block.getName()));
					pl.addErrors(errors);
				}
			}

		}

	}

	private RearYardResult compareRearYardResult(String blockName, BigDecimal min, BigDecimal mean,
			OccupancyTypeHelper mostRestrictiveOccupancy, RearYardResult rearYardResult, Boolean valid, String subRule,
			String rule, BigDecimal minVal, BigDecimal meanVal, Integer level) {
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

				if (meanVal.compareTo(rearYardResult.expectedmeanDistance) >= 0) {
					rearYardResult.expectedmeanDistance = meanVal;
					rearYardResult.actualMeanDistance = mean;
				}
			} else {
				rearYardResult.rule = rule;
				rearYardResult.occupancy = occupancyName;
				rearYardResult.expectedmeanDistance = meanVal;
				rearYardResult.actualMeanDistance = mean;

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
