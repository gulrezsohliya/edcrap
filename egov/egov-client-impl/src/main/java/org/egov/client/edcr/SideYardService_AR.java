package org.egov.client.edcr;

import static org.egov.client.constants.DxfFileConstants_AR.*;
import static org.egov.edcr.constants.DxfFileConstants.F_RT;
import static org.egov.edcr.constants.DxfFileConstants.A;
import static org.egov.edcr.constants.DxfFileConstants.A_AF;
import static org.egov.edcr.constants.DxfFileConstants.A_HE;
import static org.egov.edcr.constants.DxfFileConstants.A_R;
import static org.egov.edcr.constants.DxfFileConstants.B;
import static org.egov.edcr.constants.DxfFileConstants.D;
import static org.egov.edcr.constants.DxfFileConstants.F;
import static org.egov.edcr.constants.DxfFileConstants.F_CB;
import static org.egov.edcr.constants.DxfFileConstants.G;
import static org.egov.edcr.constants.DxfFileConstants.I;
import static org.egov.edcr.constants.DxfFileConstants.A_PO;
import static org.egov.edcr.utility.DcrConstants.FRONT_YARD_DESC;
import static org.egov.edcr.utility.DcrConstants.OBJECTNOTDEFINED;
import static org.egov.edcr.utility.DcrConstants.SIDE_YARD1_DESC;
import static org.egov.edcr.utility.DcrConstants.SIDE_YARD2_DESC;
import static org.egov.edcr.utility.DcrConstants.SIDE_YARD_DESC;

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
import org.egov.common.entity.edcr.Yard;
import org.egov.edcr.constants.DxfFileConstants;
import org.egov.edcr.feature.SideYardService;
import org.egov.infra.utils.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class SideYardService_AR extends SideYardService {

	private static final BigDecimal SIDEVALUE_ONEPOINTFIVE = BigDecimal.valueOf(1.5);
	private static final BigDecimal SIDEVALUE_ONEPOINTEIGHT = BigDecimal.valueOf(1.8);
	private static final BigDecimal SIDEVALUE_TWO = BigDecimal.valueOf(2);
	private static final BigDecimal SIDEVALUE_TWOPOINTFIVE = BigDecimal.valueOf(2.5);
	private static final BigDecimal SIDEVALUE_THREE = BigDecimal.valueOf(3);
	private static final BigDecimal SIDEVALUE_THREEPOINTSIX = BigDecimal.valueOf(3.66);
	private static final BigDecimal SIDEVALUE_FOUR = BigDecimal.valueOf(4);
	private static final BigDecimal SIDEVALUE_FOURPOINTFIVE = BigDecimal.valueOf(4.5);
	private static final BigDecimal SIDEVALUE_FIVE = BigDecimal.valueOf(5);
	private static final BigDecimal SIDEVALUE_SIX = BigDecimal.valueOf(6);
	private static final BigDecimal SIDEVALUE_SEVEN = BigDecimal.valueOf(7);
	private static final BigDecimal SIDEVALUE_SEVENTYFIVE = BigDecimal.valueOf(0.75);
	private static final BigDecimal SIDEVALUE_EIGHT = BigDecimal.valueOf(8);
	private static final BigDecimal SIDEVALUE_NINE = BigDecimal.valueOf(9);
	private static final BigDecimal SIDEVALUE_TEN = BigDecimal.valueOf(10);

	// Arunachal side yard minimum
	private static final BigDecimal SIDEVALUE_ONE = BigDecimal.valueOf(1);
	private static final BigDecimal SIDEVALUE_ONE_TWO = BigDecimal.valueOf(1.2);

	private static final String SIDENUMBER = "Side Number";
	private static final String MINIMUMLABEL = "Minimum distance ";

	private static final String RULE_35 = "35 Table-9";
	private static final String RULE_36 = "36";
	private static final String RULE_37_TWO_A = "37-2-A";
	private static final String RULE_37_TWO_B = "37-2-B";
	private static final String RULE_37_TWO_C = "37-2-C";
	private static final String RULE_37_TWO_D = "37-2-D";
	private static final String RULE_37_TWO_G = "37-2-G";
	private static final String RULE_37_TWO_H = "37-2-H";
	private static final String RULE_37_TWO_I = "37-2-I";
	private static final String RULE_47 = "47";
	private static final String SIDE_YARD_2_NOTDEFINED = "side2yardNodeDefined";
	private static final String SIDE_YARD_1_NOTDEFINED = "side1yardNodeDefined";

	public static final String BSMT_SIDE_YARD_DESC = "Basement Side Yard";
	private static final int PLOTAREA_300 = 300;
	public static final BigDecimal ROAD_WIDTH_TWELVE_POINTTWO = BigDecimal.valueOf(12.2);

	private static final Logger LOG = Logger.getLogger(SideYardService_AR.class);

	private class SideYardResult {
		String rule;
		String subRule;
		String blockName;
		Integer level;
		BigDecimal actualMeanDistance = BigDecimal.ZERO;
		BigDecimal actualDistance = BigDecimal.ZERO;
		String occupancy;
		BigDecimal expectedDistance = BigDecimal.ZERO;
		BigDecimal expectedmeanDistance = BigDecimal.ZERO;
		boolean status = false;
	}

	public void processSideYard(final Plan pl) {
		LOG.info("Arunachal Set backs SIDE YARDS !!!");
		HashMap<String, String> errors = new HashMap<>();
		Plot plot = pl.getPlot();
		if (plot == null)
			return;

		Boolean OCTYP_FOUND = false;
		Boolean OCTYP_NOTFOUND = false;

		validateSideYardRule(pl);

		// Side yard 1 and side yard 2 both may not mandatory in same levels. Get
		// previous level side yards in this case.
		// In case of side yard 1 defined and other side not required, then consider
		// other side as zero distance ( in case of noc
		// provided cases).

		Boolean valid = false;
		if (plot != null && !pl.getBlocks().isEmpty()) {
			for (Block block : pl.getBlocks()) { // for each block
				scrutinyDetail = new ScrutinyDetail();
				scrutinyDetail.addColumnHeading(1, RULE_NO);
				scrutinyDetail.addColumnHeading(2, LEVEL);
				scrutinyDetail.addColumnHeading(3, OCCUPANCY);
				scrutinyDetail.addColumnHeading(4, SIDENUMBER);
				scrutinyDetail.addColumnHeading(5, FIELDVERIFIED);
				scrutinyDetail.addColumnHeading(6, PERMISSIBLE);
				scrutinyDetail.addColumnHeading(7, PROVIDED);
				scrutinyDetail.addColumnHeading(8, STATUS);
				scrutinyDetail.setHeading(SIDE_YARD_DESC);
				SideYardResult sideYard1Result = new SideYardResult();
				SideYardResult sideYard2Result = new SideYardResult();

				for (SetBack setback : block.getSetBacks()) {
					Yard sideYard1 = null;
					Yard sideYard2 = null;

					if (setback.getSideYard1() != null
							&& setback.getSideYard1().getMean().compareTo(BigDecimal.ZERO) > 0) {
						sideYard1 = setback.getSideYard1();
					}
					if (setback.getSideYard2() != null
							&& setback.getSideYard2().getMean().compareTo(BigDecimal.ZERO) > 0) {
						sideYard2 = setback.getSideYard2();
					}

					LOG.info("sideYard1 : " + sideYard1);
					LOG.info("sideYard2 : " + sideYard2);

					BigDecimal buildingHeight;
					if (sideYard1 != null || sideYard2 != null) {
						// If there is changes in height of building, then consider the maximum height
						// among both side
						if (sideYard1 != null && sideYard1.getHeight() != null
								&& sideYard1.getHeight().compareTo(BigDecimal.ZERO) > 0 && sideYard2 != null
								&& sideYard2.getHeight() != null
								&& sideYard2.getHeight().compareTo(BigDecimal.ZERO) > 0) {
							buildingHeight = sideYard1.getHeight().compareTo(sideYard2.getHeight()) >= 0
									? sideYard1.getHeight()
									: sideYard2.getHeight();
						} else {
							buildingHeight = sideYard1 != null && sideYard1.getHeight() != null
									&& sideYard1.getHeight().compareTo(BigDecimal.ZERO) > 0
											? sideYard1.getHeight()
											: sideYard2 != null && sideYard2.getHeight() != null
													&& sideYard2.getHeight().compareTo(BigDecimal.ZERO) > 0
															? sideYard2.getHeight()
															: block.getBuilding().getBuildingHeight();
						}

						double minlength = 0;
						double max = 0;
						double minMeanlength = 0;
						double maxMeanLength = 0;
						if (sideYard2 != null && sideYard1 != null) {
							if (sideYard2.getMinimumDistance().doubleValue() > sideYard1.getMinimumDistance()
									.doubleValue()) {
								minlength = sideYard1.getMinimumDistance().doubleValue();
								max = sideYard2.getMinimumDistance().doubleValue();
							} else {
								minlength = sideYard2.getMinimumDistance().doubleValue();
								max = sideYard1.getMinimumDistance().doubleValue();
							}
						} else {
							if (sideYard1 != null) {
								max = sideYard1.getMinimumDistance().doubleValue();
							} else {
								minlength = sideYard2.getMinimumDistance().doubleValue();
							}
						}

						LOG.info("Block_" + block.getName());
						LOG.info("SIDEYARD max AP " + max);
						LOG.info("SIDEYARD minlength AP" + minlength);
						LOG.info("SIDEYARD minMeanlength AP" + minMeanlength);
						LOG.info("SIDEYARD maxMeanLength AP" + maxMeanLength);

						if (buildingHeight == null) {
							errors.put("Block_" + block.getName() + "_" + SIDE_YARD_DESC, SIDE_YARD_DESC + " for Block "
									+ block.getName() + " : unable to get building height !!");
							pl.addErrors(errors);
						} else if (buildingHeight != null && (minlength > 0 || max > 0)) {
							OCTYP_FOUND = false;
							OCTYP_NOTFOUND = false;

							System.out.println(" size of block.getBuilding().getTotalArea() : "
									+ block.getBuilding().getTotalArea().size());

							for (final Occupancy occupancy : block.getBuilding().getTotalArea()) {
								scrutinyDetail.setKey("Block_" + block.getName() + "_" + "Side Setback");

								LOG.info("    OCC type  :" + occupancy.getTypeHelper().getType());

								if (occupancy.getTypeHelper().getType() == null) {

									OCTYP_NOTFOUND = true; // occ typ not found

								} else {
									OCTYP_FOUND = true; // search for occ typ

									if ((occupancy.getTypeHelper().getType() != null
											&& A.equalsIgnoreCase(occupancy.getTypeHelper().getType().getCode()))) { // Residential

										if (sideYard1 != null) {
											sideYard1Result = checkSideYardForResidential_Side1(pl, block.getBuilding(),
													buildingHeight, block.getName(), setback.getLevel(), plot,
													minlength, max, minMeanlength, maxMeanLength,
													occupancy.getTypeHelper(), sideYard1Result, sideYard2Result);
										}

										if (sideYard2 != null) {
											sideYard2Result = checkSideYardForResidential_Side2(pl, block.getBuilding(),
													buildingHeight, block.getName(), setback.getLevel(), plot,
													minlength, max, minMeanlength, maxMeanLength,
													occupancy.getTypeHelper(), sideYard1Result, sideYard2Result);
										}

									} else if (occupancy.getTypeHelper().getType() != null
											&& F.equalsIgnoreCase(occupancy.getTypeHelper().getType().getCode())) { // Commercial

										if (sideYard1 != null) {
											sideYard1Result = checkSideYardForCommercial_Side1(pl, block.getBuilding(),
													buildingHeight, block.getName(), setback.getLevel(), plot,
													minlength, max, minMeanlength, maxMeanLength,
													occupancy.getTypeHelper(), sideYard1Result, sideYard2Result);
										}

										if (sideYard2 != null) {
											sideYard2Result = checkSideYardForCommercial_Side2(pl, block.getBuilding(),
													buildingHeight, block.getName(), setback.getLevel(), plot,
													minlength, max, minMeanlength, maxMeanLength,
													occupancy.getTypeHelper(), sideYard1Result, sideYard2Result);

										}

									} else if (occupancy.getTypeHelper().getType() != null
											&& ML.equalsIgnoreCase(occupancy.getTypeHelper().getType().getCode())) { // Mixed
																														// Landuse

										if (sideYard1 != null) {
											sideYard1Result = checkSideYardForMixedLanduse_Side1(pl,
													block.getBuilding(), buildingHeight, block.getName(),
													setback.getLevel(), plot, minlength, max, minMeanlength,
													maxMeanLength, occupancy.getTypeHelper(), sideYard1Result,
													sideYard2Result);
										}

										if (sideYard2 != null) {
											sideYard2Result = checkSideYardForMixedLanduse_Side2(pl,
													block.getBuilding(), buildingHeight, block.getName(),
													setback.getLevel(), plot, minlength, max, minMeanlength,
													maxMeanLength, occupancy.getTypeHelper(), sideYard1Result,
													sideYard2Result);
										}

									}
								}

							}

							if (OCTYP_NOTFOUND == true && OCTYP_FOUND == false) {
								errors.put("Block_" + block.getName() + "_" + SIDE_YARD_DESC, SIDE_YARD_DESC
										+ " for Block " + block.getName() + " : Occupancy Type not Found!!");
								pl.addErrors(errors);
							}

							addSideYardResult(pl, errors, sideYard1Result, sideYard2Result, sideYard1, sideYard2);
						}

						if (pl.getPlanInformation() != null
								&& pl.getPlanInformation().getWidthOfPlot().compareTo(BigDecimal.valueOf(10)) <= 0) {

							exemptSideYardForOtherOccupancyType(pl, block, sideYard1Result, sideYard2Result);
						}
					} else {
						if (pl.getPlanInformation() != null
								&& pl.getPlanInformation().getWidthOfPlot().compareTo(BigDecimal.valueOf(10)) <= 0) {

							exemptSideYardForOtherOccupancyType(pl, block, sideYard1Result, sideYard2Result);

//							if (pl.getErrors() != null) {
//								errors.put(SIDE_YARD_DESC, prepareMessage(OBJECTNOTDEFINED,
//										SIDE_YARD_DESC + " for Block " + block.getName()));
//							}

							addSideYardResult_2(pl, errors, sideYard1Result, sideYard2Result);
						}
					}

				}
			}
		}

	}

	private void addSideYardResult(final Plan pl, HashMap<String, String> errors, SideYardResult sideYard1Result,
			SideYardResult sideYard2Result, Yard sideYard1, Yard sideYard2) {

		if (errors.isEmpty()) {

			if (sideYard1Result != null && sideYard1 != null) {
				Map<String, String> details = new HashMap<>();
				details.put(RULE_NO, sideYard1Result.subRule);
				details.put(LEVEL, sideYard1Result.level != null ? sideYard1Result.level.toString() : "");
				details.put(OCCUPANCY, sideYard1Result.occupancy);

				details.put(FIELDVERIFIED, MINIMUMLABEL);
				details.put(PERMISSIBLE, sideYard1Result.expectedDistance.toString());
				details.put(PROVIDED, sideYard1Result.actualDistance.toString());

				details.put(SIDENUMBER, SIDE_YARD1_DESC);

				if (sideYard1Result.status) {
					details.put(STATUS, Result.Accepted.getResultVal());
				} else {
					details.put(STATUS, Result.Not_Accepted.getResultVal());
				}

				scrutinyDetail.getDetail().add(details);
				pl.getReportOutput().getScrutinyDetails().add(scrutinyDetail);
			}

			if (sideYard2Result != null && sideYard2 != null) {
				Map<String, String> detailsSideYard2 = new HashMap<>();
				detailsSideYard2.put(RULE_NO, sideYard2Result.subRule);
				detailsSideYard2.put(LEVEL, sideYard2Result.level != null ? sideYard2Result.level.toString() : "");
				detailsSideYard2.put(OCCUPANCY, sideYard2Result.occupancy);
				detailsSideYard2.put(SIDENUMBER, SIDE_YARD2_DESC);

				detailsSideYard2.put(FIELDVERIFIED, MINIMUMLABEL);
				detailsSideYard2.put(PERMISSIBLE, sideYard2Result.expectedDistance.toString());
				detailsSideYard2.put(PROVIDED, sideYard2Result.actualDistance.toString());
				// }
				if (sideYard2Result.status) {
					detailsSideYard2.put(STATUS, Result.Accepted.getResultVal());
				} else {
					detailsSideYard2.put(STATUS, Result.Not_Accepted.getResultVal());
				}

				scrutinyDetail.getDetail().add(detailsSideYard2);
				pl.getReportOutput().getScrutinyDetails().add(scrutinyDetail);
			}
		}
	}

	private void addSideYardResult_2(final Plan pl, HashMap<String, String> errors, SideYardResult sideYard1Result,
			SideYardResult sideYard2Result) {

		if (errors.isEmpty()) {

			if (sideYard1Result != null) {
				Map<String, String> details = new HashMap<>();
				details.put(RULE_NO, sideYard1Result.subRule);
				details.put(LEVEL, sideYard1Result.level != null ? sideYard1Result.level.toString() : "");
				details.put(OCCUPANCY, sideYard1Result.occupancy);

				details.put(FIELDVERIFIED, MINIMUMLABEL);
				details.put(PERMISSIBLE, sideYard1Result.expectedDistance.toString());
				details.put(PROVIDED, sideYard1Result.actualDistance.toString());

				details.put(SIDENUMBER, SIDE_YARD1_DESC);

				if (sideYard1Result.status) {
					details.put(STATUS, Result.Accepted.getResultVal());
				} else {
					details.put(STATUS, Result.Not_Accepted.getResultVal());
				}

				scrutinyDetail.getDetail().add(details);
				pl.getReportOutput().getScrutinyDetails().add(scrutinyDetail);
			}

			if (sideYard2Result != null) {
				Map<String, String> detailsSideYard2 = new HashMap<>();
				detailsSideYard2.put(RULE_NO, sideYard2Result.subRule);
				detailsSideYard2.put(LEVEL, sideYard2Result.level != null ? sideYard2Result.level.toString() : "");
				detailsSideYard2.put(OCCUPANCY, sideYard2Result.occupancy);
				detailsSideYard2.put(SIDENUMBER, SIDE_YARD2_DESC);

				detailsSideYard2.put(FIELDVERIFIED, MINIMUMLABEL);
				detailsSideYard2.put(PERMISSIBLE, sideYard2Result.expectedDistance.toString());
				detailsSideYard2.put(PROVIDED, sideYard2Result.actualDistance.toString());
				// }
				if (sideYard2Result.status) {
					detailsSideYard2.put(STATUS, Result.Accepted.getResultVal());
				} else {
					detailsSideYard2.put(STATUS, Result.Not_Accepted.getResultVal());
				}

				scrutinyDetail.getDetail().add(detailsSideYard2);
				pl.getReportOutput().getScrutinyDetails().add(scrutinyDetail);
			}
		}
	}

	private void exemptSideYardForOtherOccupancyType(final Plan pl, Block block, SideYardResult sideYard1Result,
			SideYardResult sideYard2Result) {
		for (final Occupancy occupancy : block.getBuilding().getTotalArea()) {
			scrutinyDetail.setKey("Block_" + block.getName() + "_" + "Side Setback");
			LOG.info(" occupancy.getTypeHelper().getSubtype() :" + occupancy.getTypeHelper().getSubtype());

			String OccupancyType = "";
			if (occupancy.getTypeHelper().getSubtype() != null) {
				if (occupancy.getTypeHelper().getSubtype().getCode() != null) {
					OccupancyType = occupancy.getTypeHelper().getSubtype().getCode();
				}

			}

			if (occupancy.getTypeHelper().getType() != null && occupancy.getTypeHelper().getSubtype() != null
					&& !(A_R.equalsIgnoreCase(OccupancyType) || A_RH.equalsIgnoreCase(OccupancyType)
							|| A_HE.equalsIgnoreCase(OccupancyType) || F_RT.equalsIgnoreCase(OccupancyType)

					)) { // When Null value accepted

				if (pl.getErrors().containsKey(SIDE_YARD_2_NOTDEFINED)) {
					pl.getErrors().remove(SIDE_YARD_2_NOTDEFINED);
				}
				if (pl.getErrors().containsKey(SIDE_YARD_1_NOTDEFINED)) {
					pl.getErrors().remove(SIDE_YARD_1_NOTDEFINED);
				}
				if (pl.getErrors().containsKey(SIDE_YARD_DESC)) {
					pl.getErrors().remove(SIDE_YARD_DESC);
				}
				if (pl.getErrors()
						.containsValue("BLK_" + block.getNumber() + "_LVL_0_SIDE_SETBACK1 not defined in the plan.")) {
					pl.getErrors().remove("",
							"BLK_" + block.getNumber() + "_LVL_0_SIDE_SETBACK1 not defined in the plan.");
				}
				if (pl.getErrors()
						.containsValue("BLK_" + block.getNumber() + "_LVL_0_SIDE_SETBACK2 not defined in the plan.")) {
					pl.getErrors().remove("",
							"BLK_" + block.getNumber() + "_LVL_0_SIDE_SETBACK2 not defined in the plan.");
				}

			}

			compareSideYard2Result(block.getName(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
					occupancy.getTypeHelper(), sideYard2Result, true, RULE_35, SIDE_YARD_DESC, 0);
			compareSideYard1Result(block.getName(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
					occupancy.getTypeHelper(), sideYard1Result, true, RULE_35, SIDE_YARD_DESC, 0);
		}
	}

	private SideYardResult checkSideYardForResidential_Side1(final Plan pl, Building building,
			BigDecimal buildingHeight, String blockName, Integer level, final Plot plot, final double min,
			final double max, double minMeanlength, double maxMeanLength,
			final OccupancyTypeHelper mostRestrictiveOccupancy, SideYardResult sideYard1Result,
			SideYardResult sideYard2Result) {

		String rule = SIDE_YARD_DESC;
		String subRule = RULE_35;

		Boolean valid1 = false;

		BigDecimal side1val = BigDecimal.ZERO;

		BigDecimal widthOfPlot = pl.getPlanInformation().getWidthOfPlot();
		BigDecimal plotArea = pl.getPlot().getArea();

		if (mostRestrictiveOccupancy.getSubtype() != null
				&& A_R.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())) { // Residential

			if (plotArea.compareTo(BigDecimal.valueOf(48)) < 0) {

			} else if (plotArea.compareTo(BigDecimal.valueOf(48)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(60)) <= 0) {

				side1val = SIDEVALUE_ONE;
			} else if (plotArea.compareTo(BigDecimal.valueOf(60)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(100)) <= 0) {

				side1val = SIDEVALUE_ONE;
			} else if (plotArea.compareTo(BigDecimal.valueOf(100)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(250)) <= 0) {

				side1val = SIDEVALUE_ONE_TWO;
			} else if (plotArea.compareTo(BigDecimal.valueOf(250)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(500)) <= 0) {

				side1val = SIDEVALUE_ONE_TWO;
			} else if (plotArea.compareTo(BigDecimal.valueOf(500)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(1000)) <= 0) {

				side1val = SIDEVALUE_ONE_TWO;
			} else if (plotArea.compareTo(BigDecimal.valueOf(1000)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(1500)) <= 0) {

				side1val = SIDEVALUE_ONE_TWO;
			} else if (plotArea.compareTo(BigDecimal.valueOf(1500)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(3000)) <= 0) {

				side1val = SIDEVALUE_ONE_TWO;
			} else if (plotArea.compareTo(BigDecimal.valueOf(3000)) > 0) {

			}
		} else if (A_AF.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())) { // Apartment
			if (plotArea.compareTo(BigDecimal.valueOf(48)) < 0) {

			} else if (plotArea.compareTo(BigDecimal.valueOf(48)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(60)) <= 0) {

				side1val = SIDEVALUE_ONE;
			} else if (plotArea.compareTo(BigDecimal.valueOf(60)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(100)) <= 0) {

				side1val = SIDEVALUE_ONE;
			} else if (plotArea.compareTo(BigDecimal.valueOf(100)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(250)) <= 0) {

				side1val = SIDEVALUE_ONE_TWO;
			} else if (plotArea.compareTo(BigDecimal.valueOf(250)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(500)) <= 0) {

				side1val = SIDEVALUE_ONE_TWO;
			} else if (plotArea.compareTo(BigDecimal.valueOf(500)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(1000)) <= 0) {

				side1val = SIDEVALUE_ONE_TWO;
			} else if (plotArea.compareTo(BigDecimal.valueOf(1000)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(1500)) <= 0) {

				side1val = SIDEVALUE_ONE_TWO;
			} else if (plotArea.compareTo(BigDecimal.valueOf(1500)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(3000)) <= 0) {

				side1val = SIDEVALUE_ONE_TWO;
			} else if (plotArea.compareTo(BigDecimal.valueOf(3000)) > 0) {

			}
		} else if (A_RH.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())) { // Row Housing

			side1val = SIDEVALUE_ONE;

		} else if (A_HE.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())
				|| A_BH.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())
				|| A_LH.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())
				|| A_GH.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())) { // Hostel/ Boarding
																								// House/Lodging House/
																								// Guest House

			if (plotArea.compareTo(BigDecimal.valueOf(100)) > 0 && plotArea.compareTo(BigDecimal.valueOf(250)) <= 0) {

				side1val = SIDEVALUE_ONE_TWO;

			} else if (plotArea.compareTo(BigDecimal.valueOf(250)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(500)) <= 0) {

				side1val = SIDEVALUE_ONE_TWO;

			} else if (plotArea.compareTo(BigDecimal.valueOf(500)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(1000)) <= 0) {

				side1val = SIDEVALUE_ONE_TWO;

			} else if (plotArea.compareTo(BigDecimal.valueOf(1000)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(1500)) <= 0) {

				side1val = SIDEVALUE_ONE_TWO;

			} else if (plotArea.compareTo(BigDecimal.valueOf(1500)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(3000)) <= 0) {

				side1val = SIDEVALUE_ONE_TWO;
			}
		}

		if (max >= side1val.doubleValue())
			valid1 = true;

		return compareSideYard1Result(blockName, side1val, BigDecimal.valueOf(max), BigDecimal.ZERO,
				BigDecimal.valueOf(maxMeanLength), mostRestrictiveOccupancy, sideYard1Result, valid1, subRule, rule,
				level);

	}

	private SideYardResult checkSideYardForResidential_Side2(final Plan pl, Building building,
			BigDecimal buildingHeight, String blockName, Integer level, final Plot plot, final double min,
			final double max, double minMeanlength, double maxMeanLength,
			final OccupancyTypeHelper mostRestrictiveOccupancy, SideYardResult sideYard1Result,
			SideYardResult sideYard2Result) {

		String rule = SIDE_YARD_DESC;
		String subRule = RULE_35;
		Boolean valid2 = false;

		BigDecimal side2val = BigDecimal.ZERO;

		BigDecimal widthOfPlot = pl.getPlanInformation().getWidthOfPlot();
		BigDecimal plotArea = pl.getPlot().getArea();

		if (mostRestrictiveOccupancy.getSubtype() != null
				&& A_R.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())) { // Residential

			if (plotArea.compareTo(BigDecimal.valueOf(48)) < 0) {

			} else if (plotArea.compareTo(BigDecimal.valueOf(48)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(60)) <= 0) {
				side2val = SIDEVALUE_ONE;

			} else if (plotArea.compareTo(BigDecimal.valueOf(60)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(100)) <= 0) {
				side2val = SIDEVALUE_ONE;

			} else if (plotArea.compareTo(BigDecimal.valueOf(100)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(250)) <= 0) {
				side2val = SIDEVALUE_ONE_TWO;

			} else if (plotArea.compareTo(BigDecimal.valueOf(250)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(500)) <= 0) {
				side2val = SIDEVALUE_ONE_TWO;

			} else if (plotArea.compareTo(BigDecimal.valueOf(500)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(1000)) <= 0) {
				side2val = SIDEVALUE_ONE_TWO;

			} else if (plotArea.compareTo(BigDecimal.valueOf(1000)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(1500)) <= 0) {
				side2val = SIDEVALUE_ONE_TWO;

			} else if (plotArea.compareTo(BigDecimal.valueOf(1500)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(3000)) <= 0) {
				side2val = SIDEVALUE_ONE_TWO;

			} else if (plotArea.compareTo(BigDecimal.valueOf(3000)) > 0) {

			}
		} else if (A_AF.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())) { // Apartment
			if (plotArea.compareTo(BigDecimal.valueOf(48)) < 0) {

			} else if (plotArea.compareTo(BigDecimal.valueOf(48)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(60)) <= 0) {
				side2val = SIDEVALUE_ONE;

			} else if (plotArea.compareTo(BigDecimal.valueOf(60)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(100)) <= 0) {
				side2val = SIDEVALUE_ONE;

			} else if (plotArea.compareTo(BigDecimal.valueOf(100)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(250)) <= 0) {
				side2val = SIDEVALUE_ONE_TWO;

			} else if (plotArea.compareTo(BigDecimal.valueOf(250)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(500)) <= 0) {
				side2val = SIDEVALUE_ONE_TWO;

			} else if (plotArea.compareTo(BigDecimal.valueOf(500)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(1000)) <= 0) {
				side2val = SIDEVALUE_ONE_TWO;

			} else if (plotArea.compareTo(BigDecimal.valueOf(1000)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(1500)) <= 0) {
				side2val = SIDEVALUE_ONE_TWO;

			} else if (plotArea.compareTo(BigDecimal.valueOf(1500)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(3000)) <= 0) {
				side2val = SIDEVALUE_ONE_TWO;

			} else if (plotArea.compareTo(BigDecimal.valueOf(3000)) > 0) {

			}
		} else if (A_RH.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())) { // Row Housing

			side2val = SIDEVALUE_ONE;

		} else if (A_HE.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())
				|| A_BH.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())
				|| A_LH.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())
				|| A_GH.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())) { // Hostel/ Boarding
																								// House/Lodging House/
																								// Guest House

			if (plotArea.compareTo(BigDecimal.valueOf(100)) > 0 && plotArea.compareTo(BigDecimal.valueOf(250)) <= 0) {

				side2val = SIDEVALUE_ONE_TWO;

			} else if (plotArea.compareTo(BigDecimal.valueOf(250)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(500)) <= 0) {

				side2val = SIDEVALUE_ONE_TWO;

			} else if (plotArea.compareTo(BigDecimal.valueOf(500)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(1000)) <= 0) {

				side2val = SIDEVALUE_ONE_TWO;

			} else if (plotArea.compareTo(BigDecimal.valueOf(1000)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(1500)) <= 0) {

				side2val = SIDEVALUE_ONE_TWO;

			} else if (plotArea.compareTo(BigDecimal.valueOf(1500)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(3000)) <= 0) {

				side2val = SIDEVALUE_ONE_TWO;
			}
		}

		if (min >= side2val.doubleValue())
			valid2 = true;

		return compareSideYard2Result(blockName, side2val, BigDecimal.valueOf(min), BigDecimal.ZERO,
				BigDecimal.valueOf(minMeanlength), mostRestrictiveOccupancy, sideYard2Result, valid2, subRule, rule,
				level);

	}

	private SideYardResult checkSideYardForCommercial_Side2(final Plan pl, Building building, BigDecimal buildingHeight,
			String blockName, Integer level, final Plot plot, final double min, final double max, double minMeanlength,
			double maxMeanLength, final OccupancyTypeHelper mostRestrictiveOccupancy, SideYardResult sideYard1Result,
			SideYardResult sideYard2Result) {

		String rule = SIDE_YARD_DESC;
		String subRule = RULE_35;
		Boolean valid2 = false;

		BigDecimal side2val = BigDecimal.ZERO;

		BigDecimal widthOfPlot = pl.getPlanInformation().getWidthOfPlot();
		BigDecimal plotArea = pl.getPlot().getArea();

		if (mostRestrictiveOccupancy.getSubtype() != null
				&& (F_RT.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())
						|| F_SH.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())
						|| F_CB.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode()))) { // Shops/Restaurant/Commercial

			if (plotArea.compareTo(BigDecimal.valueOf(48)) < 0) {

			} else if (plotArea.compareTo(BigDecimal.valueOf(48)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(100)) <= 0) {
				side2val = SIDEVALUE_ONE_TWO;

			} else if (plotArea.compareTo(BigDecimal.valueOf(100)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(250)) <= 0) {

				side2val = SIDEVALUE_ONE_TWO;

			} else if (plotArea.compareTo(BigDecimal.valueOf(250)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(500)) <= 0) {

				side2val = SIDEVALUE_ONE_TWO;

			} else if (plotArea.compareTo(BigDecimal.valueOf(500)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(1000)) <= 0) {

				side2val = SIDEVALUE_ONE_TWO;

			} else if (plotArea.compareTo(BigDecimal.valueOf(1000)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(1500)) <= 0) {

				side2val = SIDEVALUE_ONE_TWO;

			} else if (plotArea.compareTo(BigDecimal.valueOf(1500)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(3000)) <= 0) {

				side2val = SIDEVALUE_ONE_TWO;
			}

		}

		if (min >= side2val.doubleValue())
			valid2 = true;

		return compareSideYard2Result(blockName, side2val, BigDecimal.valueOf(min), BigDecimal.ZERO,
				BigDecimal.valueOf(minMeanlength), mostRestrictiveOccupancy, sideYard2Result, valid2, subRule, rule,
				level);

	}

	private SideYardResult checkSideYardForCommercial_Side1(final Plan pl, Building building, BigDecimal buildingHeight,
			String blockName, Integer level, final Plot plot, final double min, final double max, double minMeanlength,
			double maxMeanLength, final OccupancyTypeHelper mostRestrictiveOccupancy, SideYardResult sideYard1Result,
			SideYardResult sideYard2Result) {

		String rule = SIDE_YARD_DESC;
		String subRule = RULE_35;

		Boolean valid1 = false;

		BigDecimal side1val = BigDecimal.ZERO;

		BigDecimal widthOfPlot = pl.getPlanInformation().getWidthOfPlot();
		BigDecimal plotArea = pl.getPlot().getArea();

		if (mostRestrictiveOccupancy.getSubtype() != null
				&& (F_RT.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())
						|| F_SH.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())
						|| F_CB.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode()))) { // Shops/Restaurant/Commercial

			if (plotArea.compareTo(BigDecimal.valueOf(48)) < 0) {

			} else if (plotArea.compareTo(BigDecimal.valueOf(48)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(100)) <= 0) {
				side1val = SIDEVALUE_ONE_TWO;

			} else if (plotArea.compareTo(BigDecimal.valueOf(100)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(250)) <= 0) {

				side1val = SIDEVALUE_ONE_TWO;

			} else if (plotArea.compareTo(BigDecimal.valueOf(250)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(500)) <= 0) {

				side1val = SIDEVALUE_ONE_TWO;

			} else if (plotArea.compareTo(BigDecimal.valueOf(500)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(1000)) <= 0) {

				side1val = SIDEVALUE_ONE_TWO;

			} else if (plotArea.compareTo(BigDecimal.valueOf(1000)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(1500)) <= 0) {

				side1val = SIDEVALUE_ONE_TWO;

			} else if (plotArea.compareTo(BigDecimal.valueOf(1500)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(3000)) <= 0) {

				side1val = SIDEVALUE_ONE_TWO;
			}

		}

		if (max >= side1val.doubleValue())
			valid1 = true;

		return compareSideYard1Result(blockName, side1val, BigDecimal.valueOf(max), BigDecimal.ZERO,
				BigDecimal.valueOf(maxMeanLength), mostRestrictiveOccupancy, sideYard1Result, valid1, subRule, rule,
				level);

	}

	private SideYardResult checkSideYardForMixedLanduse_Side1(final Plan pl, Building building,
			BigDecimal buildingHeight, String blockName, Integer level, final Plot plot, final double min,
			final double max, double minMeanlength, double maxMeanLength,
			final OccupancyTypeHelper mostRestrictiveOccupancy, SideYardResult sideYard1Result,
			SideYardResult sideYard2Result) {

		String rule = SIDE_YARD_DESC;
		String subRule = RULE_35;

		Boolean valid1 = false;

		BigDecimal side1val = BigDecimal.ZERO;

		BigDecimal widthOfPlot = pl.getPlanInformation().getWidthOfPlot();
		BigDecimal plotArea = pl.getPlot().getArea();

		if (mostRestrictiveOccupancy.getSubtype() != null
				&& ML_A_R.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())) { // Residential

			if (plotArea.compareTo(BigDecimal.valueOf(48)) < 0) {

			} else if (plotArea.compareTo(BigDecimal.valueOf(48)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(60)) <= 0) {

				side1val = SIDEVALUE_ONE;
			} else if (plotArea.compareTo(BigDecimal.valueOf(60)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(100)) <= 0) {

				side1val = SIDEVALUE_ONE;
			} else if (plotArea.compareTo(BigDecimal.valueOf(100)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(250)) <= 0) {

				side1val = SIDEVALUE_ONE_TWO;
			} else if (plotArea.compareTo(BigDecimal.valueOf(250)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(500)) <= 0) {

				side1val = SIDEVALUE_ONE_TWO;
			} else if (plotArea.compareTo(BigDecimal.valueOf(500)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(1000)) <= 0) {

				side1val = SIDEVALUE_ONE_TWO;
			} else if (plotArea.compareTo(BigDecimal.valueOf(1000)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(1500)) <= 0) {

				side1val = SIDEVALUE_ONE_TWO;
			} else if (plotArea.compareTo(BigDecimal.valueOf(1500)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(3000)) <= 0) {

				side1val = SIDEVALUE_ONE_TWO;
			} else if (plotArea.compareTo(BigDecimal.valueOf(3000)) > 0) {

			}
		} else if (ML_A_AF.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())) { // Apartment
			if (plotArea.compareTo(BigDecimal.valueOf(48)) < 0) {

			} else if (plotArea.compareTo(BigDecimal.valueOf(48)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(60)) <= 0) {

				side1val = SIDEVALUE_ONE;
			} else if (plotArea.compareTo(BigDecimal.valueOf(60)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(100)) <= 0) {

				side1val = SIDEVALUE_ONE;
			} else if (plotArea.compareTo(BigDecimal.valueOf(100)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(250)) <= 0) {

				side1val = SIDEVALUE_ONE_TWO;
			} else if (plotArea.compareTo(BigDecimal.valueOf(250)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(500)) <= 0) {

				side1val = SIDEVALUE_ONE_TWO;
			} else if (plotArea.compareTo(BigDecimal.valueOf(500)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(1000)) <= 0) {

				side1val = SIDEVALUE_ONE_TWO;
			} else if (plotArea.compareTo(BigDecimal.valueOf(1000)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(1500)) <= 0) {

				side1val = SIDEVALUE_ONE_TWO;
			} else if (plotArea.compareTo(BigDecimal.valueOf(1500)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(3000)) <= 0) {

				side1val = SIDEVALUE_ONE_TWO;
			} else if (plotArea.compareTo(BigDecimal.valueOf(3000)) > 0) {

			}
		} else if (ML_A_RH.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())) { // Row Housing

			side1val = SIDEVALUE_ONE;

		} else if (ML_A_HE.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())
				|| ML_A_BH.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())) { // Hostel/ Boarding
																								// House

			if (plotArea.compareTo(BigDecimal.valueOf(500)) < 0) {

				side1val = SIDEVALUE_ONE_TWO;

			} else if (plotArea.compareTo(BigDecimal.valueOf(500)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(1000)) <= 0) {

				side1val = SIDEVALUE_ONE_TWO;

			} else if (plotArea.compareTo(BigDecimal.valueOf(1000)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(2000)) <= 0) {

				side1val = SIDEVALUE_ONE_TWO;

			} else if (plotArea.compareTo(BigDecimal.valueOf(2000)) > 0) {

				side1val = SIDEVALUE_ONE_TWO;
			}
		}

		if (mostRestrictiveOccupancy.getSubtype() != null
				&& (ML_F_RT.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())
						|| ML_F_SH.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode()))) {

			if (plotArea.compareTo(BigDecimal.valueOf(48)) < 0) {

			} else if (plotArea.compareTo(BigDecimal.valueOf(48)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(100)) <= 0) {

				side1val = SIDEVALUE_ONE;

			} else if (plotArea.compareTo(BigDecimal.valueOf(100)) > 0) {

				side1val = SIDEVALUE_ONE_TWO;

			}

		}

		if (max >= side1val.doubleValue())
			valid1 = true;

		return compareSideYard1Result(blockName, side1val, BigDecimal.valueOf(max), BigDecimal.ZERO,
				BigDecimal.valueOf(maxMeanLength), mostRestrictiveOccupancy, sideYard1Result, valid1, subRule, rule,
				level);

	}

	private SideYardResult checkSideYardForMixedLanduse_Side2(final Plan pl, Building building,
			BigDecimal buildingHeight, String blockName, Integer level, final Plot plot, final double min,
			final double max, double minMeanlength, double maxMeanLength,
			final OccupancyTypeHelper mostRestrictiveOccupancy, SideYardResult sideYard1Result,
			SideYardResult sideYard2Result) {

		String rule = SIDE_YARD_DESC;
		String subRule = RULE_35;
		Boolean valid2 = false;

		BigDecimal side2val = BigDecimal.ZERO;

		BigDecimal widthOfPlot = pl.getPlanInformation().getWidthOfPlot();
		BigDecimal plotArea = pl.getPlot().getArea();

		if (mostRestrictiveOccupancy.getSubtype() != null
				&& ML_A_R.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())) { // Residential

			if (plotArea.compareTo(BigDecimal.valueOf(48)) < 0) {

			} else if (plotArea.compareTo(BigDecimal.valueOf(48)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(60)) <= 0) {
				side2val = SIDEVALUE_ONE;

			} else if (plotArea.compareTo(BigDecimal.valueOf(60)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(100)) <= 0) {
				side2val = SIDEVALUE_ONE;

			} else if (plotArea.compareTo(BigDecimal.valueOf(100)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(250)) <= 0) {
				side2val = SIDEVALUE_ONE_TWO;

			} else if (plotArea.compareTo(BigDecimal.valueOf(250)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(500)) <= 0) {
				side2val = SIDEVALUE_ONE_TWO;

			} else if (plotArea.compareTo(BigDecimal.valueOf(500)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(1000)) <= 0) {
				side2val = SIDEVALUE_ONE_TWO;

			} else if (plotArea.compareTo(BigDecimal.valueOf(1000)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(1500)) <= 0) {
				side2val = SIDEVALUE_ONE_TWO;

			} else if (plotArea.compareTo(BigDecimal.valueOf(1500)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(3000)) <= 0) {
				side2val = SIDEVALUE_ONE_TWO;

			} else if (plotArea.compareTo(BigDecimal.valueOf(3000)) > 0) {

			}
		} else if (ML_A_AF.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())) { // Apartment
			if (plotArea.compareTo(BigDecimal.valueOf(48)) < 0) {

			} else if (plotArea.compareTo(BigDecimal.valueOf(48)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(60)) <= 0) {
				side2val = SIDEVALUE_ONE;

			} else if (plotArea.compareTo(BigDecimal.valueOf(60)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(100)) <= 0) {
				side2val = SIDEVALUE_ONE;

			} else if (plotArea.compareTo(BigDecimal.valueOf(100)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(250)) <= 0) {
				side2val = SIDEVALUE_ONE_TWO;

			} else if (plotArea.compareTo(BigDecimal.valueOf(250)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(500)) <= 0) {
				side2val = SIDEVALUE_ONE_TWO;

			} else if (plotArea.compareTo(BigDecimal.valueOf(500)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(1000)) <= 0) {
				side2val = SIDEVALUE_ONE_TWO;

			} else if (plotArea.compareTo(BigDecimal.valueOf(1000)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(1500)) <= 0) {
				side2val = SIDEVALUE_ONE_TWO;

			} else if (plotArea.compareTo(BigDecimal.valueOf(1500)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(3000)) <= 0) {
				side2val = SIDEVALUE_ONE_TWO;

			} else if (plotArea.compareTo(BigDecimal.valueOf(3000)) > 0) {

			}
		} else if (ML_A_RH.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())) { // Row Housing

			side2val = SIDEVALUE_ONE;

		} else if (ML_A_HE.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())
				|| ML_A_BH.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())) { // Hostel/ Boarding
																								// House

			if (plotArea.compareTo(BigDecimal.valueOf(500)) < 0) {
				side2val = SIDEVALUE_ONE_TWO;

			} else if (plotArea.compareTo(BigDecimal.valueOf(500)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(1000)) <= 0) {
				side2val = SIDEVALUE_ONE_TWO;

			} else if (plotArea.compareTo(BigDecimal.valueOf(1000)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(2000)) <= 0) {
				side2val = SIDEVALUE_ONE_TWO;

			} else if (plotArea.compareTo(BigDecimal.valueOf(2000)) > 0) {
				side2val = SIDEVALUE_ONE_TWO;

			}
		}

		if (mostRestrictiveOccupancy.getSubtype() != null
				&& (ML_F_RT.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode())
						|| ML_F_SH.equalsIgnoreCase(mostRestrictiveOccupancy.getSubtype().getCode()))) {

			if (plotArea.compareTo(BigDecimal.valueOf(48)) < 0) {

			} else if (plotArea.compareTo(BigDecimal.valueOf(48)) > 0
					&& plotArea.compareTo(BigDecimal.valueOf(100)) <= 0) {
				side2val = SIDEVALUE_ONE;

			} else if (plotArea.compareTo(BigDecimal.valueOf(100)) > 0) {
				side2val = SIDEVALUE_ONE_TWO;

			}

		}

		if (min >= side2val.doubleValue())
			valid2 = true;

		return compareSideYard2Result(blockName, side2val, BigDecimal.valueOf(min), BigDecimal.ZERO,
				BigDecimal.valueOf(minMeanlength), mostRestrictiveOccupancy, sideYard2Result, valid2, subRule, rule,
				level);

	}

	private SideYardResult compareSideYard1Result(String blockName, BigDecimal exptDistance, BigDecimal actualDistance,
			BigDecimal expectedMeanDistance, BigDecimal actualMeanDistance,
			OccupancyTypeHelper mostRestrictiveOccupancy, SideYardResult sideYard1Result, Boolean valid, String subRule,
			String rule, Integer level) {
		String occupancyName;
		if (mostRestrictiveOccupancy.getSubtype() != null)
			occupancyName = mostRestrictiveOccupancy.getSubtype().getName();
		else if (mostRestrictiveOccupancy.getType() != null)
			occupancyName = mostRestrictiveOccupancy.getType().getName();
		else
			occupancyName = "";

		if (exptDistance.compareTo(sideYard1Result.expectedDistance) >= 0) {
			if (exptDistance.compareTo(sideYard1Result.expectedDistance) == 0) {
				sideYard1Result.rule = sideYard1Result.rule != null ? sideYard1Result.rule + "," + rule : rule;
				sideYard1Result.occupancy = sideYard1Result.occupancy != null
						? sideYard1Result.occupancy + "," + occupancyName
						: occupancyName;
			} else {
				sideYard1Result.rule = rule;
				sideYard1Result.occupancy = occupancyName;
			}

			sideYard1Result.subRule = subRule;
			sideYard1Result.blockName = blockName;
			sideYard1Result.level = level;
			sideYard1Result.actualDistance = actualDistance;
			sideYard1Result.expectedDistance = exptDistance;
			sideYard1Result.status = valid;
		}
		return sideYard1Result;
	}

	private SideYardResult compareSideYard2Result(String blockName, BigDecimal exptDistance, BigDecimal actualDistance,
			BigDecimal expectedMeanDistance, BigDecimal actualMeanDistance,
			OccupancyTypeHelper mostRestrictiveOccupancy, SideYardResult sideYard2Result, Boolean valid, String subRule,
			String rule, Integer level) {
		String occupancyName;
		if (mostRestrictiveOccupancy.getSubtype() != null)
			occupancyName = mostRestrictiveOccupancy.getSubtype().getName();
		else if (mostRestrictiveOccupancy.getType() != null)
			occupancyName = mostRestrictiveOccupancy.getType().getName();
		else
			occupancyName = "";

		if (exptDistance.compareTo(sideYard2Result.expectedDistance) >= 0) {
			if (exptDistance.compareTo(sideYard2Result.expectedDistance) == 0) {

				sideYard2Result.rule = sideYard2Result.rule != null ? sideYard2Result.rule + "," + rule : rule;
				sideYard2Result.occupancy = sideYard2Result.occupancy != null
						? sideYard2Result.occupancy + "," + occupancyName
						: occupancyName;

			} else {
				sideYard2Result.rule = rule;
				sideYard2Result.occupancy = occupancyName;
			}

			sideYard2Result.subRule = subRule;
			sideYard2Result.blockName = blockName;
			sideYard2Result.level = level;
			sideYard2Result.actualDistance = actualDistance;
			sideYard2Result.expectedDistance = exptDistance;
			sideYard2Result.status = valid;
		}

		return sideYard2Result;
	}

	private void validateSideYardRule(final Plan pl) {

		for (Block block : pl.getBlocks()) {
			if (!block.getCompletelyExisting()) {
				Boolean sideYardDefined = false;
				for (SetBack setback : block.getSetBacks()) {
					if (setback.getSideYard1() != null
							&& setback.getSideYard1().getMean().compareTo(BigDecimal.valueOf(0)) > 0) {
						sideYardDefined = true;
					} else if (setback.getSideYard2() != null
							&& setback.getSideYard2().getMean().compareTo(BigDecimal.valueOf(0)) > 0) {
						sideYardDefined = true;
					}
				}
				if (!sideYardDefined) {
					HashMap<String, String> errors = new HashMap<>();
					errors.put(SIDE_YARD_DESC,
							prepareMessage(OBJECTNOTDEFINED, SIDE_YARD_DESC + " for Block " + block.getName()));
					pl.addErrors(errors);
				}
			}

		}

	}

}
