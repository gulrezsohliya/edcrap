package org.egov.client.edcr;

import static org.egov.edcr.constants.DxfFileConstants.A;
import static org.egov.edcr.constants.DxfFileConstants.A_R;
import static org.egov.edcr.constants.DxfFileConstants.A_AF;
import static org.egov.edcr.constants.DxfFileConstants.A_HE;
import static org.egov.edcr.constants.DxfFileConstants.B_PS;
import static org.egov.edcr.constants.DxfFileConstants.F_RT;
import static org.egov.edcr.constants.DxfFileConstants.F_H;
import static org.egov.edcr.constants.DxfFileConstants.F_CB;
import static org.egov.edcr.constants.DxfFileConstants.F;
import static org.egov.edcr.constants.DxfFileConstants.G;
import static org.egov.edcr.constants.DxfFileConstants.C;
import static org.egov.edcr.constants.DxfFileConstants.B;
import static org.egov.edcr.constants.DxfFileConstants.PARKING_SLOT;
import static org.egov.edcr.utility.DcrConstants.SQMTRS;

import static org.egov.client.constants.DxfFileConstants_AR.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.common.entity.edcr.Block;
import org.egov.common.entity.edcr.Building;
import org.egov.common.entity.edcr.Floor;
import org.egov.common.entity.edcr.Measurement;
import org.egov.common.entity.edcr.Occupancy;
import org.egov.common.entity.edcr.OccupancyType;
import org.egov.common.entity.edcr.OccupancyTypeHelper;
import org.egov.common.entity.edcr.ParkingDetails;
import org.egov.common.entity.edcr.ParkingHelper;
import org.egov.common.entity.edcr.Plan;
import org.egov.common.entity.edcr.Result;
import org.egov.common.entity.edcr.ScrutinyDetail;
import org.egov.edcr.feature.Parking;
import org.egov.edcr.utility.DcrConstants;
import org.egov.edcr.utility.Util;
import org.jfree.util.Log;
import org.springframework.stereotype.Service;

@Service
public class Parking_AR extends Parking {

	private static final Logger LOGGER = Logger.getLogger(Parking_AR.class);

	private static final String OUT_OF = "Out of ";
	private static final String SLOT_HAVING_GT_4_PTS = " number of polygon not having only 4 points.";
	private static final String LOADING_UNLOADING_DESC = "Minimum required Loading/Unloading area";
	private static final String MINIMUM_AREA_OF_EACH_DA_PARKING = " Minimum width of Each Special parking";
	private static final String SP_PARKING_SLOT_AREA = "Special Parking Area";
	private static final String NO_VIOLATION_OF_AREA = "No violation of area in ";
	private static final String MIN_AREA_EACH_CAR_PARKING = " Minimum Area of Each ECS parking";
	private static final String PARKING_VIOLATED_MINIMUM_AREA = " parking violated minimum area.";
	private static final String PARKING = " parking ";
	private static final String NUMBERS = " Numbers ";
	private static final String MECHANICAL_PARKING = "Mechanical parking";
	private static final String MAX_ALLOWED_MECH_PARK = "Maximum allowed mechanical parking";
	private static final String TWO_WHEELER_PARK_AREA = "Two Wheeler Parking Area";
	private static final String LOADING_UNLOADING_AREA = "Loading Unloading Area";
	private static final String SP_PARKING = "Special parking";
	private static final String SUB_RULE_34_1_DESCRIPTION = "Parking Slots Area";
	private static final String SUB_RULE_34_2 = "34-2";
	private static final String SUB_RULE_40_8 = "40-8";
	private static final String SUB_RULE_40_11 = "40-11";
	private static final String PARKING_MIN_AREA = "5 M x 2 M";
	private static final double PARKING_SLOT_WIDTH = 2;
	private static final double PARKING_SLOT_HEIGHT = 5;
	private static final double SP_PARK_SLOT_MIN_SIDE = 3.6;
	private static final String DA_PARKING_MIN_AREA = " 3.60 M ";
	public static final String NO_OF_UNITS = "No of apartment units";
	private static final double TWO_WHEEL_PARKING_AREA_WIDTH = 1.5;
	private static final double TWO_WHEEL_PARKING_AREA_HEIGHT = 2.0;
	private static final double MECH_PARKING_WIDTH = 2.7;
	private static final double MECH_PARKING_HEIGHT = 5.5;

	private static final double OPEN_ECS = 23;
	private static final double COVER_ECS = 28;
	private static final double BSMNT_ECS = 32;
	private static final double PARK_A = 0.25;
	private static final double PARK_F = 0.30;
	private static final double PARK_VISITOR = 0.15;
	private static final String SUB_RULE_40 = "40";
	private static final String SUB_RULE_40_2 = "40-2";
	private static final String SUB_RULE_40_2_DESCRIPTION = "Parking space";
	private static final String SUB_RULE_40_10 = "40-10";
	private static final String SUB_RULE_40_10_DESCRIPTION = "Visitor’s Parking";
	public static final String OPEN_PARKING_DIM_DESC = "Open parking ECS dimension ";
	public static final String COVER_PARKING_DIM_DESC = "Cover parking ECS dimension ";
	public static final String BSMNT_PARKING_DIM_DESC = "Basement parking ECS dimension ";
	public static final String VISITOR_PARKING = "Visitor parking";
	public static final String SPECIAL_PARKING_DIM_DESC = "Special parking ECS dimension ";
	public static final String TWO_WHEELER_DIM_DESC = "Two wheeler parking dimension ";
	public static final String MECH_PARKING_DESC = "Mechanical parking dimension ";
	public static final String MECH_PARKING_DIM_DESC = "All Mechanical parking polylines should have dimension 2.7*5.5 m²";
	public static final String MECH_PARKING_DIM_DESC_NA = " mechanical parking polyines does not have dimensions 2.7*5.5 m²";

	private static final String PARKING_VIOLATED_DIM = " parking violated dimension.";
	private static final String PARKING_AREA_DIM = "1.5 M x 2 M";

	// arunachal ecs values
	private static final double OPEN_ECS_18 = 18;

	private static final BigDecimal AREA_PER_ECS_100 = BigDecimal.valueOf(100);

	private static final double ECS_ZERO_POINT_FIVE = 0.5;
	private static final double ECS_ONE_POINT_ZERO = 1.0;
	private static final double ECS_ONE_POINT_FIVE = 1.5;
	private static final double ECS_TWO_POINT_ZERO = 2.0;
	private static final double ECS_TWO_POINT_FIVE = 2.5;
	private static final double ECS_THREE_POINT_ZERO = 3.0;

	@Override
	public Plan validate(Plan pl) {
		validateDimensions(pl);
		return pl;
	}

	@Override
	public Plan process(Plan pl) {
		Log.info(" Arunachal  Pradesh Parking Process!!");
		try {
			validate(pl);
			scrutinyDetail = new ScrutinyDetail();
			scrutinyDetail.setKey("Common_Parking");
			scrutinyDetail.addColumnHeading(1, RULE_NO);
			scrutinyDetail.addColumnHeading(2, DESCRIPTION);
			scrutinyDetail.addColumnHeading(3, REQUIRED);
			scrutinyDetail.addColumnHeading(4, PROVIDED);
			scrutinyDetail.addColumnHeading(5, STATUS);
			processParking(pl);
		}catch (Exception e) {
			// TODO: handle exception
		}
		

		return pl;
	}

	private void validateDimensions(Plan pl) {
//		ParkingDetails parkDtls = pl.getParkingDetails();
//		if (!parkDtls.getCars().isEmpty()) {
//			int count = 0;
//			for (Measurement m : parkDtls.getCars())
//				if (m.getInvalidReason() != null && m.getInvalidReason().length() > 0)
//					count++;
//			if (count > 0)
//				pl.addError(PARKING_SLOT, PARKING_SLOT + count + SLOT_HAVING_GT_4_PTS);
//		}
//
//		if (!parkDtls.getOpenCars().isEmpty()) {
//			int count = 0;
//			for (Measurement m : parkDtls.getOpenCars())
//				if (m.getInvalidReason() != null && m.getInvalidReason().length() > 0)
//					count++;
//			if (count > 0)
//				pl.addError(OPEN_PARKING_DIM_DESC, OPEN_PARKING_DIM_DESC + count + SLOT_HAVING_GT_4_PTS);
//		}
//
//		if (!parkDtls.getCoverCars().isEmpty()) {
//			int count = 0;
//			for (Measurement m : parkDtls.getCoverCars())
//				if (m.getInvalidReason() != null && m.getInvalidReason().length() > 0)
//					count++;
//			if (count > 0)
//				pl.addError(COVER_PARKING_DIM_DESC, COVER_PARKING_DIM_DESC + count + SLOT_HAVING_GT_4_PTS);
//		}
//
//		if (!parkDtls.getCoverCars().isEmpty()) {
//			int count = 0;
//			for (Measurement m : parkDtls.getBasementCars())
//				if (m.getInvalidReason() != null && m.getInvalidReason().length() > 0)
//					count++;
//			if (count > 0)
//				pl.addError(BSMNT_PARKING_DIM_DESC, BSMNT_PARKING_DIM_DESC + count + SLOT_HAVING_GT_4_PTS);
//		}
//

	}

	public void processParking(Plan pl) {

//		LOGGER.info("Parking Arunachal Pradesh ");
//		ParkingHelper helper = new ParkingHelper();
//		// checkDimensionForCarParking(pl, helper);
//
//		OccupancyTypeHelper mostRestrictiveOccupancy = pl.getVirtualBuilding() != null
//				? pl.getVirtualBuilding().getMostRestrictiveFarHelper()
//				: null;
//		BigDecimal totalBuiltupArea = pl.getOccupancies().stream().map(Occupancy::getBuiltUpArea)
//				.reduce(BigDecimal.ZERO, BigDecimal::add);
//
//		BigDecimal totalFloorArea = pl.getOccupancies().stream().map(Occupancy::getFloorArea).reduce(BigDecimal.ZERO,
//				BigDecimal::add);
//
//		BigDecimal totalFloorArea2 = pl.getVirtualBuilding().getTotalFloorArea();
//
//		BigDecimal plotArea = pl.getPlot().getArea();
//
//
//		BigDecimal coverParkingArea = BigDecimal.ZERO;
////		BigDecimal stiltParkingArea = BigDecimal.ZERO;
//		BigDecimal basementParkingArea = BigDecimal.ZERO;
//		for (Block block : pl.getBlocks()) {
//			for (Floor floor : block.getBuilding().getFloors()) {
//				coverParkingArea = coverParkingArea.add(floor.getParking().getCoverCars().stream()
//						.map(Measurement::getArea).reduce(BigDecimal.ZERO, BigDecimal::add));
//
////				stiltParkingArea = stiltParkingArea.add(floor.getParking().getStilts().stream()
////						.map(Measurement::getArea).reduce(BigDecimal.ZERO, BigDecimal::add));
//
//				basementParkingArea = basementParkingArea.add(floor.getParking().getBasementCars().stream()
//						.map(Measurement::getArea).reduce(BigDecimal.ZERO, BigDecimal::add));
//			}
//		}
//		BigDecimal openParkingArea = pl.getParkingDetails().getOpenCars().stream().map(Measurement::getArea)
//				.reduce(BigDecimal.ZERO, BigDecimal::add);
//
//		BigDecimal totalProvidedCarParkArea = openParkingArea.add(coverParkingArea).add(basementParkingArea);
//		helper.totalRequiredCarParking += openParkingArea.doubleValue() / OPEN_ECS_18;
//		helper.totalRequiredCarParking += coverParkingArea.doubleValue() / COVER_ECS;
//		helper.totalRequiredCarParking += basementParkingArea.doubleValue() / BSMNT_ECS;
////		helper.totalRequiredCarParking += stiltParkingArea.doubleValue() / COVER_ECS;
//
//		LOGGER.info(" totalProvidedCarParkArea : " + totalProvidedCarParkArea);
//
//		Double requiredCarParkArea = 0d;
//		Double requiredVisitorParkArea = 0d;
//
//		BigDecimal providedVisitorParkArea = BigDecimal.ZERO;
//
////        validateSpecialParking(pl, helper, totalBuiltupArea);
//
//		if (mostRestrictiveOccupancy != null) {
//			if (mostRestrictiveOccupancy != null && totalBuiltupArea != null
//					&& A.equals(mostRestrictiveOccupancy.getType().getCode())) {
//
//				if ((A_R.equals(mostRestrictiveOccupancy.getSubtype().getCode())
//						|| A_AF.equals(mostRestrictiveOccupancy.getSubtype().getCode())
//						|| A_RH.equals(mostRestrictiveOccupancy.getSubtype().getCode()))) {
//
//					if (plotArea.compareTo(BigDecimal.valueOf(100)) < 0) {
//
//					} else if (plotArea.compareTo(BigDecimal.valueOf(100)) > 0
//							&& plotArea.compareTo(BigDecimal.valueOf(300)) <= 0) {
//
//						if (coverParkingArea.doubleValue() > 0)
//							requiredCarParkArea += COVER_ECS * ECS_ONE_POINT_FIVE;
//						if (basementParkingArea.doubleValue() > 0)
//							requiredCarParkArea += BSMNT_ECS * ECS_ONE_POINT_FIVE;
//						if (openParkingArea.doubleValue() > 0)
//							requiredCarParkArea += OPEN_ECS_18 * ECS_ONE_POINT_FIVE;
//					} else if (plotArea.compareTo(BigDecimal.valueOf(300)) > 0
//							&& plotArea.compareTo(BigDecimal.valueOf(500)) <= 0) {
//
//						if (coverParkingArea.doubleValue() > 0)
//							requiredCarParkArea += COVER_ECS * ECS_TWO_POINT_FIVE;
//						if (basementParkingArea.doubleValue() > 0)
//							requiredCarParkArea += BSMNT_ECS * ECS_TWO_POINT_FIVE;
//						if (openParkingArea.doubleValue() > 0)
//							requiredCarParkArea += OPEN_ECS_18 * ECS_TWO_POINT_FIVE;
//					} else if (plotArea.compareTo(BigDecimal.valueOf(500)) > 0) {
//						BigDecimal builtupArea = totalBuiltupArea;
////								.subtract(totalBuiltupArea.multiply(BigDecimal.valueOf(0.15)));
//						double requiredEcs = builtupArea.divide(AREA_PER_ECS_100)
//								.multiply(BigDecimal.valueOf(ECS_ONE_POINT_ZERO)).setScale(0, RoundingMode.UP)
//								.doubleValue();
//
//						if (coverParkingArea.doubleValue() > 0)
//							requiredCarParkArea += COVER_ECS * requiredEcs;
//						if (basementParkingArea.doubleValue() > 0)
//							requiredCarParkArea += BSMNT_ECS * requiredEcs;
//						if (openParkingArea.doubleValue() > 0)
//							requiredCarParkArea += OPEN_ECS_18 * requiredEcs;
//					}
//				} else if (A_HE.equals(mostRestrictiveOccupancy.getSubtype().getCode())
//						|| A_BH.equals(mostRestrictiveOccupancy.getSubtype().getCode())
//						|| A_LH.equals(mostRestrictiveOccupancy.getSubtype().getCode())
//						|| A_GH.equals(mostRestrictiveOccupancy.getSubtype().getCode())) {
//
//					if (plotArea.compareTo(BigDecimal.valueOf(100)) < 0) {
//
//					} else if (plotArea.compareTo(BigDecimal.valueOf(100)) > 0) {
//
//						if (coverParkingArea.doubleValue() > 0)
//							requiredCarParkArea += COVER_ECS * ECS_ONE_POINT_FIVE;
//						if (basementParkingArea.doubleValue() > 0)
//							requiredCarParkArea += BSMNT_ECS * ECS_ONE_POINT_FIVE;
//						if (openParkingArea.doubleValue() > 0)
//							requiredCarParkArea += OPEN_ECS_18 * ECS_ONE_POINT_FIVE;
//					}
//
//				}
//
//			} else if (mostRestrictiveOccupancy != null && F.equals(mostRestrictiveOccupancy.getType().getCode())) {
//				BigDecimal floorArea = totalFloorArea;
//				double requiredEcs = 0.0;
//
//				double requiredEcs2 = 0.0;
//
//				if (mostRestrictiveOccupancy != null && (F_RT.equals(mostRestrictiveOccupancy.getSubtype().getCode())
//						|| F_SH.equals(mostRestrictiveOccupancy.getSubtype().getCode())
//						|| F_CB.equals(mostRestrictiveOccupancy.getSubtype().getCode()))) {
//					requiredEcs = floorArea.divide(AREA_PER_ECS_100).multiply(BigDecimal.valueOf(ECS_ZERO_POINT_FIVE))
//							.setScale(0, RoundingMode.UP).doubleValue();
//					requiredEcs2 = floorArea.divide(AREA_PER_ECS_100).multiply(BigDecimal.valueOf(ECS_ONE_POINT_FIVE))
//							.setScale(0, RoundingMode.UP).doubleValue();
//
//				} else if (F_SM.equals(mostRestrictiveOccupancy.getSubtype().getCode())) {
//					requiredEcs = floorArea.divide(AREA_PER_ECS_100).multiply(BigDecimal.valueOf(ECS_THREE_POINT_ZERO))
//							.setScale(0, RoundingMode.UP).doubleValue();
//
//				} else if (F_H.equals(mostRestrictiveOccupancy.getSubtype().getCode())) {
//					requiredEcs = floorArea.divide(AREA_PER_ECS_100).multiply(BigDecimal.valueOf(ECS_TWO_POINT_ZERO))
//							.setScale(0, RoundingMode.UP).doubleValue();
//					requiredEcs2 = floorArea.divide(AREA_PER_ECS_100).multiply(BigDecimal.valueOf(ECS_THREE_POINT_ZERO))
//							.setScale(0, RoundingMode.UP).doubleValue();
//				} else if (F_WST.equals(mostRestrictiveOccupancy.getSubtype().getCode())
//						|| F_WH.equals(mostRestrictiveOccupancy.getSubtype().getCode())) {
//					requiredEcs = floorArea.divide(AREA_PER_ECS_100).multiply(BigDecimal.valueOf(ECS_TWO_POINT_ZERO))
//							.setScale(0, RoundingMode.UP).doubleValue();
//				}
//				LOGGER.info("requiredEcs " + requiredEcs);
//				LOGGER.info("requiredEcs2 " + requiredEcs2);
//
//				if (coverParkingArea.doubleValue() > 0)
//					requiredCarParkArea += COVER_ECS * requiredEcs;
//				if (basementParkingArea.doubleValue() > 0)
//					requiredCarParkArea += BSMNT_ECS * requiredEcs;
//				if (openParkingArea.doubleValue() > 0)
//					requiredCarParkArea += OPEN_ECS_18 * requiredEcs;
//
//			} else if (mostRestrictiveOccupancy != null && ML.equals(mostRestrictiveOccupancy.getType().getCode())) {
//				// MIXED LANDUSE
//
//				if ((ML_A_R.equals(mostRestrictiveOccupancy.getSubtype().getCode())
//						|| ML_A_AF.equals(mostRestrictiveOccupancy.getSubtype().getCode())
//						|| ML_A_RH.equals(mostRestrictiveOccupancy.getSubtype().getCode())
//						|| ML_A_HE.equals(mostRestrictiveOccupancy.getSubtype().getCode())
//						|| ML_A_BH.equals(mostRestrictiveOccupancy.getSubtype().getCode())
//						|| ML_A_LH.equals(mostRestrictiveOccupancy.getSubtype().getCode())
//						|| ML_A_GH.equals(mostRestrictiveOccupancy.getSubtype().getCode()))) {
//
//					if (plotArea.compareTo(BigDecimal.valueOf(100)) < 0) {
//
//					} else if (plotArea.compareTo(BigDecimal.valueOf(100)) > 0
//							&& plotArea.compareTo(BigDecimal.valueOf(300)) <= 0) {
//
//						if (coverParkingArea.doubleValue() > 0)
//							requiredCarParkArea += COVER_ECS * ECS_ONE_POINT_FIVE;
//						if (basementParkingArea.doubleValue() > 0)
//							requiredCarParkArea += BSMNT_ECS * ECS_ONE_POINT_FIVE;
//						if (openParkingArea.doubleValue() > 0)
//							requiredCarParkArea += OPEN_ECS_18 * ECS_ONE_POINT_FIVE;
//					} else if (plotArea.compareTo(BigDecimal.valueOf(300)) > 0
//							&& plotArea.compareTo(BigDecimal.valueOf(500)) <= 0) {
//
//						if (coverParkingArea.doubleValue() > 0)
//							requiredCarParkArea += COVER_ECS * ECS_TWO_POINT_FIVE;
//						if (basementParkingArea.doubleValue() > 0)
//							requiredCarParkArea += BSMNT_ECS * ECS_TWO_POINT_FIVE;
//						if (openParkingArea.doubleValue() > 0)
//							requiredCarParkArea += OPEN_ECS_18 * ECS_TWO_POINT_FIVE;
//					} else if (plotArea.compareTo(BigDecimal.valueOf(500)) > 0) {
//						BigDecimal builtupArea = totalBuiltupArea;
//
//						double requiredEcs = builtupArea.divide(AREA_PER_ECS_100)
//								.multiply(BigDecimal.valueOf(ECS_ONE_POINT_ZERO)).setScale(0, RoundingMode.UP)
//								.doubleValue();
//
//						if (coverParkingArea.doubleValue() > 0)
//							requiredCarParkArea += COVER_ECS * requiredEcs;
//						if (basementParkingArea.doubleValue() > 0)
//							requiredCarParkArea += BSMNT_ECS * requiredEcs;
//						if (openParkingArea.doubleValue() > 0)
//							requiredCarParkArea += OPEN_ECS_18 * requiredEcs;
//					}
//				}
//
//				if (mostRestrictiveOccupancy != null && (ML_F_RT.equals(mostRestrictiveOccupancy.getSubtype().getCode())
//						|| ML_F_SH.equals(mostRestrictiveOccupancy.getSubtype().getCode())
//						|| ML_F_SM.equals(mostRestrictiveOccupancy.getSubtype().getCode())
//						|| ML_F_H.equals(mostRestrictiveOccupancy.getSubtype().getCode())
//						|| ML_F_WST.equals(mostRestrictiveOccupancy.getSubtype().getCode())
//						|| ML_F_WH.equals(mostRestrictiveOccupancy.getSubtype().getCode()))) {
//
//					BigDecimal floorArea = totalFloorArea;
//					double requiredEcs = 0.0;
//
//					double requiredEcs2 = 0.0;
//
//					if (mostRestrictiveOccupancy != null
//							&& (ML_F_RT.equals(mostRestrictiveOccupancy.getSubtype().getCode())
//									|| ML_F_SH.equals(mostRestrictiveOccupancy.getSubtype().getCode()))) {
//						requiredEcs = floorArea.divide(AREA_PER_ECS_100)
//								.multiply(BigDecimal.valueOf(ECS_ZERO_POINT_FIVE)).setScale(0, RoundingMode.UP)
//								.doubleValue();
//						requiredEcs2 = floorArea.divide(AREA_PER_ECS_100)
//								.multiply(BigDecimal.valueOf(ECS_ONE_POINT_FIVE)).setScale(0, RoundingMode.UP)
//								.doubleValue();
//
//					} else if (ML_F_SM.equals(mostRestrictiveOccupancy.getSubtype().getCode())) {
//						requiredEcs = floorArea.divide(AREA_PER_ECS_100)
//								.multiply(BigDecimal.valueOf(ECS_THREE_POINT_ZERO)).setScale(0, RoundingMode.UP)
//								.doubleValue();
//
//					} else if (ML_F_H.equals(mostRestrictiveOccupancy.getSubtype().getCode())) {
//						requiredEcs = floorArea.divide(AREA_PER_ECS_100)
//								.multiply(BigDecimal.valueOf(ECS_TWO_POINT_ZERO)).setScale(0, RoundingMode.UP)
//								.doubleValue();
//						requiredEcs2 = floorArea.divide(AREA_PER_ECS_100)
//								.multiply(BigDecimal.valueOf(ECS_THREE_POINT_ZERO)).setScale(0, RoundingMode.UP)
//								.doubleValue();
//					} else if (ML_F_WST.equals(mostRestrictiveOccupancy.getSubtype().getCode())
//							|| ML_F_WH.equals(mostRestrictiveOccupancy.getSubtype().getCode())) {
//						requiredEcs = floorArea.divide(AREA_PER_ECS_100)
//								.multiply(BigDecimal.valueOf(ECS_TWO_POINT_ZERO)).setScale(0, RoundingMode.UP)
//								.doubleValue();
//					}
//					LOGGER.info("requiredEcs " + requiredEcs);
//					LOGGER.info("requiredEcs2 " + requiredEcs2);
//
//					if (coverParkingArea.doubleValue() > 0)
//						requiredCarParkArea += COVER_ECS * requiredEcs;
//					if (basementParkingArea.doubleValue() > 0)
//						requiredCarParkArea += BSMNT_ECS * requiredEcs;
//					if (openParkingArea.doubleValue() > 0)
//						requiredCarParkArea += OPEN_ECS_18 * requiredEcs;
//
//				}
//
//			} else if (mostRestrictiveOccupancy != null && G.equals(mostRestrictiveOccupancy.getType().getCode())) {
//				if (mostRestrictiveOccupancy != null && (G_I.equals(mostRestrictiveOccupancy.getSubtype().getCode()))) { // Industrial
//					// Use Floor area instead of built up area
//
//					BigDecimal floorArea = totalFloorArea;
//
//
//					double requiredEcs = 0.0;
//
//					if (plotArea.compareTo(BigDecimal.valueOf(50)) <= 0) {
//
//						requiredEcs = floorArea.divide(AREA_PER_ECS_100)
//								.multiply(BigDecimal.valueOf(ECS_ONE_POINT_ZERO)).setScale(0, RoundingMode.UP)
//								.doubleValue();
//
//					} else if (plotArea.compareTo(BigDecimal.valueOf(50)) > 0
//							&& plotArea.compareTo(BigDecimal.valueOf(400)) <= 0) {
//
//						requiredEcs = floorArea.divide(AREA_PER_ECS_100)
//								.multiply(BigDecimal.valueOf(ECS_TWO_POINT_ZERO)).setScale(0, RoundingMode.UP)
//								.doubleValue();
//
//					} else if (plotArea.compareTo(BigDecimal.valueOf(400)) > 0) {
//
//						requiredEcs = floorArea.divide(AREA_PER_ECS_100)
//								.multiply(BigDecimal.valueOf(ECS_TWO_POINT_ZERO)).setScale(0, RoundingMode.UP)
//								.doubleValue();
//
//					}
//
//					if (coverParkingArea.doubleValue() > 0)
//						requiredCarParkArea += COVER_ECS * requiredEcs;
//					if (basementParkingArea.doubleValue() > 0)
//						requiredCarParkArea += BSMNT_ECS * requiredEcs;
//					if (openParkingArea.doubleValue() > 0)
//						requiredCarParkArea += OPEN_ECS_18 * requiredEcs;
//
//				} else if (G_FI.equals(mostRestrictiveOccupancy.getSubtype().getCode())
//						|| G_SC.equals(mostRestrictiveOccupancy.getSubtype().getCode())) { // Flatted Industry/ Service
//																							// Center
//					// Use Floor area instead of built up area *****
//
//					BigDecimal floorArea = totalFloorArea;
//
//
//					double requiredEcs = floorArea.divide(AREA_PER_ECS_100)
//							.multiply(BigDecimal.valueOf(ECS_TWO_POINT_ZERO)).setScale(0, RoundingMode.UP)
//							.doubleValue();
//
//					if (coverParkingArea.doubleValue() > 0)
//						requiredCarParkArea += COVER_ECS * requiredEcs;
//					if (basementParkingArea.doubleValue() > 0)
//						requiredCarParkArea += BSMNT_ECS * requiredEcs;
//					if (openParkingArea.doubleValue() > 0)
//						requiredCarParkArea += OPEN_ECS_18 * requiredEcs;
//				}
//
//			} else if (mostRestrictiveOccupancy != null && T.equals(mostRestrictiveOccupancy.getType().getCode())) { // Transportation
//
//				BigDecimal floorArea = totalFloorArea;
//			
//				double requiredEcs = 0.0;
//
//				if (T_R.equals(mostRestrictiveOccupancy.getSubtype().getCode())) {// Rail Terminal
//
//					requiredEcs = floorArea.divide(AREA_PER_ECS_100).multiply(BigDecimal.valueOf(ECS_TWO_POINT_ZERO))
//							.setScale(0, RoundingMode.UP).doubleValue();
//				} else if (T_I.equals(mostRestrictiveOccupancy.getSubtype().getCode())
//						|| T_B.equals(mostRestrictiveOccupancy.getSubtype().getCode())) {// ISBT/Bus Terminal
//
//					requiredEcs = floorArea.divide(AREA_PER_ECS_100).multiply(BigDecimal.valueOf(ECS_TWO_POINT_ZERO))
//							.setScale(0, RoundingMode.UP).doubleValue();
//				}
//
//				if (coverParkingArea.doubleValue() > 0)
//					requiredCarParkArea += COVER_ECS * requiredEcs;
//				if (basementParkingArea.doubleValue() > 0)
//					requiredCarParkArea += BSMNT_ECS * requiredEcs;
//				if (openParkingArea.doubleValue() > 0)
//					requiredCarParkArea += OPEN_ECS_18 * requiredEcs;
//
//			} else if (mostRestrictiveOccupancy != null && P.equals(mostRestrictiveOccupancy.getType().getCode())) { // Public
//																														// &
//																														// Semi
//																														// Public
//
//				BigDecimal floorArea = totalFloorArea;
//
//				double requiredEcs = 0.0;
//				if (P_O.equals(mostRestrictiveOccupancy.getSubtype().getCode())
//						|| P_I.equals(mostRestrictiveOccupancy.getSubtype().getCode())) {
//
//					requiredEcs = floorArea.divide(AREA_PER_ECS_100).multiply(BigDecimal.valueOf(ECS_TWO_POINT_ZERO))
//							.setScale(0, RoundingMode.UP).doubleValue();
//				} else if (P_D.equals(mostRestrictiveOccupancy.getSubtype().getCode())) {
//
//					requiredEcs = floorArea.divide(AREA_PER_ECS_100).multiply(BigDecimal.valueOf(ECS_TWO_POINT_ZERO))
//							.setScale(0, RoundingMode.UP).doubleValue();
//				} else if (P_H.equals(mostRestrictiveOccupancy.getSubtype().getCode())
//						|| P_A.equals(mostRestrictiveOccupancy.getSubtype().getCode())
//						|| P_B.equals(mostRestrictiveOccupancy.getSubtype().getCode())) {
//
//					requiredEcs = floorArea.divide(AREA_PER_ECS_100).multiply(BigDecimal.valueOf(ECS_TWO_POINT_ZERO))
//							.setScale(0, RoundingMode.UP).doubleValue();
//				} else if (P_C.equals(mostRestrictiveOccupancy.getSubtype().getCode())) {
//
//					requiredEcs = floorArea.divide(AREA_PER_ECS_100).multiply(BigDecimal.valueOf(ECS_TWO_POINT_ZERO))
//							.setScale(0, RoundingMode.UP).doubleValue();
//				}
//
//				if (coverParkingArea.doubleValue() > 0)
//					requiredCarParkArea += COVER_ECS * requiredEcs;
//				if (basementParkingArea.doubleValue() > 0)
//					requiredCarParkArea += BSMNT_ECS * requiredEcs;
//				if (openParkingArea.doubleValue() > 0)
//					requiredCarParkArea += OPEN_ECS_18 * requiredEcs;
//
//			} else if (mostRestrictiveOccupancy != null && C.equals(mostRestrictiveOccupancy.getType().getCode())) {// Health
//																													// Services
//
//				BigDecimal floorArea = totalFloorArea;
//				
//				double requiredEcs = 0.0;
//
//				if (C_H.equals(mostRestrictiveOccupancy.getSubtype().getCode())
//						|| C_T.equals(mostRestrictiveOccupancy.getSubtype().getCode())) {
//					requiredEcs = floorArea.divide(AREA_PER_ECS_100).multiply(BigDecimal.valueOf(ECS_TWO_POINT_ZERO))
//							.setScale(0, RoundingMode.UP).doubleValue();
//				} else if (C_NH.equals(mostRestrictiveOccupancy.getSubtype().getCode())
//						|| C_PC.equals(mostRestrictiveOccupancy.getSubtype().getCode())
//						|| C_D.equals(mostRestrictiveOccupancy.getSubtype().getCode())
//						|| C_DC.equals(mostRestrictiveOccupancy.getSubtype().getCode())) {
//					requiredEcs = floorArea.divide(AREA_PER_ECS_100).multiply(BigDecimal.valueOf(ECS_TWO_POINT_ZERO))
//							.setScale(0, RoundingMode.UP).doubleValue();
//				} else if (C_VH.equals(mostRestrictiveOccupancy.getSubtype().getCode())
//						|| C_VD.equals(mostRestrictiveOccupancy.getSubtype().getCode())) {
//					requiredEcs = floorArea.divide(AREA_PER_ECS_100).multiply(BigDecimal.valueOf(ECS_ONE_POINT_ZERO))
//							.setScale(0, RoundingMode.UP).doubleValue();
//				} else if (C_NAPI.equals(mostRestrictiveOccupancy.getSubtype().getCode())) {
//					requiredEcs = floorArea.divide(AREA_PER_ECS_100).multiply(BigDecimal.valueOf(ECS_TWO_POINT_ZERO))
//							.setScale(0, RoundingMode.UP).doubleValue();
//				}
//
//				if (coverParkingArea.doubleValue() > 0)
//					requiredCarParkArea += COVER_ECS * requiredEcs;
//				if (basementParkingArea.doubleValue() > 0)
//					requiredCarParkArea += BSMNT_ECS * requiredEcs;
//				if (openParkingArea.doubleValue() > 0)
//					requiredCarParkArea += OPEN_ECS_18 * requiredEcs;
//
//			} else if (mostRestrictiveOccupancy != null && B.equals(mostRestrictiveOccupancy.getType().getCode())) { // Education
//
//				BigDecimal floorArea = totalFloorArea;
//				
//				double requiredEcs = 0.0;
//				if (B_NS.equals(mostRestrictiveOccupancy.getSubtype().getCode())) {
//					requiredEcs = floorArea.divide(AREA_PER_ECS_100).multiply(BigDecimal.valueOf(ECS_TWO_POINT_ZERO))
//							.setScale(0, RoundingMode.UP).doubleValue();
//				} else if (B_PS.equals(mostRestrictiveOccupancy.getSubtype().getCode())
//						|| B_UPS.equals(mostRestrictiveOccupancy.getSubtype().getCode())) {
//					requiredEcs = floorArea.divide(AREA_PER_ECS_100).multiply(BigDecimal.valueOf(ECS_TWO_POINT_ZERO))
//							.setScale(0, RoundingMode.UP).doubleValue();
//				} else if (B_SS.equals(mostRestrictiveOccupancy.getSubtype().getCode())
//						|| B_HSS.equals(mostRestrictiveOccupancy.getSubtype().getCode())) {
//					requiredEcs = floorArea.divide(AREA_PER_ECS_100).multiply(BigDecimal.valueOf(ECS_TWO_POINT_ZERO))
//							.setScale(0, RoundingMode.UP).doubleValue();
//				} else if (B_C.equals(mostRestrictiveOccupancy.getSubtype().getCode())
//						|| B_U.equals(mostRestrictiveOccupancy.getSubtype().getCode())) {
//					requiredEcs = floorArea.divide(AREA_PER_ECS_100).multiply(BigDecimal.valueOf(ECS_TWO_POINT_ZERO))
//							.setScale(0, RoundingMode.UP).doubleValue();
//				} else if (B_SFMC.equals(mostRestrictiveOccupancy.getSubtype().getCode())) {
//					requiredEcs = floorArea.divide(AREA_PER_ECS_100).multiply(BigDecimal.valueOf(ECS_TWO_POINT_ZERO))
//							.setScale(0, RoundingMode.UP).doubleValue();
//
//				} else if (B_ERIC.equals(mostRestrictiveOccupancy.getSubtype().getCode())) {
//
//					if (B_ERIC_AC.equals(mostRestrictiveOccupancy.getUsage().getCode())) {
//						requiredEcs = floorArea.divide(AREA_PER_ECS_100)
//								.multiply(BigDecimal.valueOf(ECS_TWO_POINT_ZERO)).setScale(0, RoundingMode.UP)
//								.doubleValue();
//					} else if (B_ERIC_AR.equals(mostRestrictiveOccupancy.getUsage().getCode())) {
//						requiredEcs = floorArea.divide(AREA_PER_ECS_100)
//								.multiply(BigDecimal.valueOf(ECS_TWO_POINT_ZERO)).setScale(0, RoundingMode.UP)
//								.doubleValue();
//					} else if (B_ERIC_SCC.equals(mostRestrictiveOccupancy.getUsage().getCode())) {
//						requiredEcs = floorArea.divide(AREA_PER_ECS_100)
//								.multiply(BigDecimal.valueOf(ECS_TWO_POINT_ZERO)).setScale(0, RoundingMode.UP)
//								.doubleValue();
//					} else if (B_ERIC_POS.equals(mostRestrictiveOccupancy.getUsage().getCode())) {
//						requiredEcs = floorArea.divide(AREA_PER_ECS_100)
//								.multiply(BigDecimal.valueOf(ECS_TWO_POINT_ZERO)).setScale(0, RoundingMode.UP)
//								.doubleValue();
//					}
//
//				} else if (B_SP.equals(mostRestrictiveOccupancy.getSubtype().getCode())) {
//					requiredEcs = floorArea.divide(AREA_PER_ECS_100).multiply(BigDecimal.valueOf(ECS_TWO_POINT_ZERO))
//							.setScale(0, RoundingMode.UP).doubleValue();
//				}
//
//				if (coverParkingArea.doubleValue() > 0)
//					requiredCarParkArea += COVER_ECS * requiredEcs;
//				if (basementParkingArea.doubleValue() > 0)
//					requiredCarParkArea += BSMNT_ECS * requiredEcs;
//				if (openParkingArea.doubleValue() > 0)
//					requiredCarParkArea += OPEN_ECS_18 * requiredEcs;
//
//			} else if (mostRestrictiveOccupancy != null && U.equals(mostRestrictiveOccupancy.getType().getCode())) { // Security
//																														// Services
//				BigDecimal floorArea = totalFloorArea;
//				double requiredEcs = 0.0;
//				if (U_PP.equals(mostRestrictiveOccupancy.getSubtype().getCode())) {
//					requiredEcs = floorArea.divide(AREA_PER_ECS_100).multiply(BigDecimal.valueOf(ECS_TWO_POINT_ZERO))
//							.setScale(0, RoundingMode.UP).doubleValue();
//				} else if (U_PS.equals(mostRestrictiveOccupancy.getSubtype().getCode())) {
//					if (U_PS_DOB.equals(mostRestrictiveOccupancy.getUsage().getCode())) {
//						requiredEcs = floorArea.divide(AREA_PER_ECS_100)
//								.multiply(BigDecimal.valueOf(ECS_TWO_POINT_ZERO)).setScale(0, RoundingMode.UP)
//								.doubleValue();
//					} else if (U_PS_DJ.equals(mostRestrictiveOccupancy.getUsage().getCode())) {
//						requiredEcs = floorArea.divide(AREA_PER_ECS_100)
//								.multiply(BigDecimal.valueOf(ECS_TWO_POINT_ZERO)).setScale(0, RoundingMode.UP)
//								.doubleValue();
//					} else if (U_PS_PTI.equals(mostRestrictiveOccupancy.getUsage().getCode())
//							|| U_PS_PTC.equals(mostRestrictiveOccupancy.getUsage().getCode())) {
//						requiredEcs = floorArea.divide(AREA_PER_ECS_100)
//								.multiply(BigDecimal.valueOf(ECS_TWO_POINT_ZERO)).setScale(0, RoundingMode.UP)
//								.doubleValue();
//					}
//				} else if (U_DMC.equals(mostRestrictiveOccupancy.getSubtype().getCode())) {
//					requiredEcs = floorArea.divide(AREA_PER_ECS_100).multiply(BigDecimal.valueOf(ECS_TWO_POINT_ZERO))
//							.setScale(0, RoundingMode.UP).doubleValue();
//				} else if (U_FP.equals(mostRestrictiveOccupancy.getSubtype().getCode())
//						|| U_FS.equals(mostRestrictiveOccupancy.getSubtype().getCode())
//						|| U_FTI.equals(mostRestrictiveOccupancy.getSubtype().getCode())
//						|| U_FTC.equals(mostRestrictiveOccupancy.getSubtype().getCode())) {
//					requiredEcs = floorArea.divide(AREA_PER_ECS_100).multiply(BigDecimal.valueOf(ECS_TWO_POINT_ZERO))
//							.setScale(0, RoundingMode.UP).doubleValue();
//				}
//
//				if (coverParkingArea.doubleValue() > 0)
//					requiredCarParkArea += COVER_ECS * requiredEcs;
//				if (basementParkingArea.doubleValue() > 0)
//					requiredCarParkArea += BSMNT_ECS * requiredEcs;
//				if (openParkingArea.doubleValue() > 0)
//					requiredCarParkArea += OPEN_ECS_18 * requiredEcs;
//
//			} else {
//
//			}
//		}
//
//		BigDecimal requiredCarParkingArea = Util.roundOffTwoDecimal(BigDecimal.valueOf(requiredCarParkArea));
//		BigDecimal totalProvidedCarParkingArea = Util.roundOffTwoDecimal(totalProvidedCarParkArea);
////		BigDecimal requiredVisitorParkingArea = Util.roundOffTwoDecimal(BigDecimal.valueOf(requiredVisitorParkArea));
////		BigDecimal providedVisitorParkingArea = Util.roundOffTwoDecimal(providedVisitorParkArea);
//
//		LOGGER.info(" requiredCarParkingArea" + requiredCarParkingArea);
//		LOGGER.info(" totalProvidedCarParkingArea" + totalProvidedCarParkingArea);
//
//		// checkDimensionForTwoWheelerParking(pl, helper);
//		// checkAreaForLoadUnloadSpaces(pl);
//		if (totalProvidedCarParkArea.doubleValue() == 0) {
//			pl.addError(SUB_RULE_40_2_DESCRIPTION,
//					getLocaleMessage("msg.error.not.defined", SUB_RULE_40_2_DESCRIPTION));
//		} else if (requiredCarParkArea > 0 && totalProvidedCarParkingArea.compareTo(requiredCarParkingArea) < 0) {
//			setReportOutputDetails(pl, SUB_RULE_40_2, SUB_RULE_40_2_DESCRIPTION, requiredCarParkingArea + SQMTRS,
//					totalProvidedCarParkingArea + SQMTRS, Result.Not_Accepted.getResultVal());
//		} else {
//			setReportOutputDetails(pl, SUB_RULE_40_2, SUB_RULE_40_2_DESCRIPTION, requiredCarParkingArea + SQMTRS,
//					totalProvidedCarParkingArea + SQMTRS, Result.Accepted.getResultVal());
//		}
////		if (requiredVisitorParkArea > 0 && providedVisitorParkArea.compareTo(requiredVisitorParkingArea) < 0) {
////			setReportOutputDetails(pl, SUB_RULE_40_10, SUB_RULE_40_10_DESCRIPTION, requiredVisitorParkingArea + SQMTRS,
////					providedVisitorParkArea + SQMTRS, Result.Not_Accepted.getResultVal());
////		} else if (requiredVisitorParkArea > 0) {
////			setReportOutputDetails(pl, SUB_RULE_40_10, SUB_RULE_40_10_DESCRIPTION, requiredVisitorParkingArea + SQMTRS,
////					providedVisitorParkingArea + SQMTRS, Result.Accepted.getResultVal());
////		}
//
//		LOGGER.info("******************Require no of Car Parking***************" + helper.totalRequiredCarParking);
	}

	private void setReportOutputDetails(Plan pl, String ruleNo, String ruleDesc, String expected, String actual,
			String status) {
		Map<String, String> details = new HashMap<>();
		details.put(RULE_NO, ruleNo);
		details.put(DESCRIPTION, ruleDesc);
		details.put(REQUIRED, expected);
		details.put(PROVIDED, actual);
		details.put(STATUS, status);
		scrutinyDetail.getDetail().add(details);
		pl.getReportOutput().getScrutinyDetails().add(scrutinyDetail);
	}

	private void processTwoWheelerParking(Plan pl, ParkingHelper helper) {
		helper.twoWheelerParking = BigDecimal.valueOf(0.25 * helper.totalRequiredCarParking * 2.70 * 5.50)
				.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
		double providedArea = 0;
		for (Measurement measurement : pl.getParkingDetails().getTwoWheelers()) {
			providedArea = providedArea + measurement.getArea().doubleValue();
		}
		if (providedArea < helper.twoWheelerParking) {
			setReportOutputDetails(pl, SUB_RULE_34_2, TWO_WHEELER_PARK_AREA,
					helper.twoWheelerParking + " " + DcrConstants.SQMTRS,
					BigDecimal.valueOf(providedArea).setScale(2, BigDecimal.ROUND_HALF_UP) + " " + DcrConstants.SQMTRS,
					Result.Not_Accepted.getResultVal());
		} else {
			setReportOutputDetails(pl, SUB_RULE_34_2, TWO_WHEELER_PARK_AREA,
					helper.twoWheelerParking + " " + DcrConstants.SQMTRS,
					BigDecimal.valueOf(providedArea).setScale(2, BigDecimal.ROUND_HALF_UP) + " " + DcrConstants.SQMTRS,
					Result.Accepted.getResultVal());
		}
	}

	/*
	 * private void buildResultForYardValidation(Plan Plan, BigDecimal
	 * parkSlotAreaInFrontYard, BigDecimal maxAllowedArea, String type) {
	 * Plan.reportOutput .add(buildRuleOutputWithSubRule(DcrConstants.RULE34,
	 * SUB_RULE_34_1,
	 * "Parking space should not exceed 50% of the area of mandatory " + type,
	 * "Parking space should not exceed 50% of the area of mandatory " + type,
	 * "Maximum allowed area for parking in " + type +" " + maxAllowedArea +
	 * DcrConstants.SQMTRS, "Parking provided in more than the allowed area " +
	 * parkSlotAreaInFrontYard + DcrConstants.SQMTRS, Result.Not_Accepted, null)); }
	 * private BigDecimal validateParkingSlotsAreWithInYard(Plan Plan, Polygon
	 * yardPolygon) { BigDecimal area = BigDecimal.ZERO; for (Measurement
	 * parkingSlot : Plan.getParkingDetails().getCars()) { Iterator parkSlotIterator
	 * = parkingSlot.getPolyLine().getVertexIterator(); while
	 * (parkSlotIterator.hasNext()) { DXFVertex dxfVertex = (DXFVertex)
	 * parkSlotIterator.next(); Point point = dxfVertex.getPoint(); if
	 * (rayCasting.contains(point, yardPolygon)) { area =
	 * area.add(parkingSlot.getArea()); } } } return area; }
	 */

	private void checkDimensionForCarParking(Plan pl, ParkingHelper helper) {

		/*
		 * for (Block block : Plan.getBlocks()) { for (SetBack setBack :
		 * block.getSetBacks()) { if (setBack.getFrontYard() != null &&
		 * setBack.getFrontYard().getPresentInDxf()) { Polygon frontYardPolygon =
		 * ProcessHelper.getPolygon(setBack.getFrontYard().getPolyLine()); BigDecimal
		 * parkSlotAreaInFrontYard = validateParkingSlotsAreWithInYard(Plan,
		 * frontYardPolygon); BigDecimal maxAllowedArea =
		 * setBack.getFrontYard().getArea().divide(BigDecimal.valueOf(2),
		 * DcrConstants.DECIMALDIGITS_MEASUREMENTS, RoundingMode.HALF_UP); if
		 * (parkSlotAreaInFrontYard.compareTo(maxAllowedArea) > 0) {
		 * buildResultForYardValidation(Plan, parkSlotAreaInFrontYard, maxAllowedArea,
		 * "front yard space"); } } if (setBack.getRearYard() != null &&
		 * setBack.getRearYard().getPresentInDxf()) { Polygon rearYardPolygon =
		 * ProcessHelper.getPolygon(setBack.getRearYard().getPolyLine()); BigDecimal
		 * parkSlotAreaInRearYard = validateParkingSlotsAreWithInYard(Plan,
		 * rearYardPolygon); BigDecimal maxAllowedArea =
		 * setBack.getRearYard().getArea().divide(BigDecimal.valueOf(2),
		 * DcrConstants.DECIMALDIGITS_MEASUREMENTS, RoundingMode.HALF_UP); if
		 * (parkSlotAreaInRearYard.compareTo(maxAllowedArea) > 0) {
		 * buildResultForYardValidation(Plan, parkSlotAreaInRearYard, maxAllowedArea,
		 * "rear yard space"); } } if (setBack.getSideYard1() != null &&
		 * setBack.getSideYard1().getPresentInDxf()) { Polygon sideYard1Polygon =
		 * ProcessHelper.getPolygon(setBack.getSideYard1().getPolyLine()); BigDecimal
		 * parkSlotAreaInSideYard1 = validateParkingSlotsAreWithInYard(Plan,
		 * sideYard1Polygon); BigDecimal maxAllowedArea =
		 * setBack.getSideYard1().getArea().divide(BigDecimal.valueOf(2),
		 * DcrConstants.DECIMALDIGITS_MEASUREMENTS, RoundingMode.HALF_UP); if
		 * (parkSlotAreaInSideYard1.compareTo(maxAllowedArea) > 0) {
		 * buildResultForYardValidation(Plan, parkSlotAreaInSideYard1, maxAllowedArea,
		 * "side yard1 space"); } } if (setBack.getSideYard2() != null &&
		 * setBack.getSideYard2().getPresentInDxf()) { Polygon sideYard2Polygon =
		 * ProcessHelper.getPolygon(setBack.getSideYard2().getPolyLine()); BigDecimal
		 * parkSlotAreaInFrontYard = validateParkingSlotsAreWithInYard(Plan,
		 * sideYard2Polygon); BigDecimal maxAllowedArea =
		 * setBack.getSideYard2().getArea().divide(BigDecimal.valueOf(2),
		 * DcrConstants.DECIMALDIGITS_MEASUREMENTS, RoundingMode.HALF_UP); if
		 * (parkSlotAreaInFrontYard.compareTo(maxAllowedArea) > 0) {
		 * buildResultForYardValidation(Plan, parkSlotAreaInFrontYard, maxAllowedArea,
		 * "side yard2 space"); } } } }
		 */

		int parkingCount = pl.getParkingDetails().getCars().size();
		int failedCount = 0;
		int success = 0;
		for (Measurement slot : pl.getParkingDetails().getCars()) {
			if (slot.getHeight().setScale(2, RoundingMode.UP).doubleValue() >= PARKING_SLOT_HEIGHT
					&& slot.getWidth().setScale(2, RoundingMode.UP).doubleValue() >= PARKING_SLOT_WIDTH)
				success++;
			else
				failedCount++;
		}
		pl.getParkingDetails().setValidCarParkingSlots(parkingCount - failedCount);
		if (parkingCount > 0)
			if (failedCount > 0) {
				if (helper.totalRequiredCarParking.intValue() == pl.getParkingDetails().getValidCarParkingSlots()) {
					setReportOutputDetails(pl, SUB_RULE_40, SUB_RULE_34_1_DESCRIPTION,
							PARKING_MIN_AREA + MIN_AREA_EACH_CAR_PARKING,
							OUT_OF + parkingCount + PARKING + failedCount + PARKING_VIOLATED_MINIMUM_AREA,
							Result.Accepted.getResultVal());
				} else {
					setReportOutputDetails(pl, SUB_RULE_40, SUB_RULE_34_1_DESCRIPTION,
							PARKING_MIN_AREA + MIN_AREA_EACH_CAR_PARKING,
							OUT_OF + parkingCount + PARKING + failedCount + PARKING_VIOLATED_MINIMUM_AREA,
							Result.Not_Accepted.getResultVal());
				}
			} else {
				setReportOutputDetails(pl, SUB_RULE_40, SUB_RULE_34_1_DESCRIPTION,
						PARKING_MIN_AREA + MIN_AREA_EACH_CAR_PARKING, NO_VIOLATION_OF_AREA + parkingCount + PARKING,
						Result.Accepted.getResultVal());
			}
		int openParkCount = pl.getParkingDetails().getOpenCars().size();
		int openFailedCount = 0;
		int openSuccess = 0;
		for (Measurement slot : pl.getParkingDetails().getOpenCars()) {
			if (slot.getHeight().setScale(2, RoundingMode.UP).doubleValue() >= PARKING_SLOT_HEIGHT
					&& slot.getWidth().setScale(2, RoundingMode.UP).doubleValue() >= PARKING_SLOT_WIDTH)
				openSuccess++;
			else
				openFailedCount++;
		}
		pl.getParkingDetails().setValidOpenCarSlots(openParkCount - openFailedCount);
		if (openParkCount > 0)
			if (openFailedCount > 0) {
				if (helper.totalRequiredCarParking.intValue() == pl.getParkingDetails().getValidOpenCarSlots()) {
					setReportOutputDetails(pl, SUB_RULE_40, OPEN_PARKING_DIM_DESC,
							PARKING_MIN_AREA + MIN_AREA_EACH_CAR_PARKING,
							OUT_OF + openParkCount + PARKING + openFailedCount + PARKING_VIOLATED_MINIMUM_AREA,
							Result.Accepted.getResultVal());
				} else {
					setReportOutputDetails(pl, SUB_RULE_40, OPEN_PARKING_DIM_DESC,
							PARKING_MIN_AREA + MIN_AREA_EACH_CAR_PARKING,
							OUT_OF + openParkCount + PARKING + openFailedCount + PARKING_VIOLATED_MINIMUM_AREA,
							Result.Not_Accepted.getResultVal());
				}
			} else {
				setReportOutputDetails(pl, SUB_RULE_40, OPEN_PARKING_DIM_DESC,
						PARKING_MIN_AREA + MIN_AREA_EACH_CAR_PARKING, NO_VIOLATION_OF_AREA + openParkCount + PARKING,
						Result.Accepted.getResultVal());
			}

		int coverParkCount = pl.getParkingDetails().getCoverCars().size();
		int coverFailedCount = 0;
		int coverSuccess = 0;
		for (Measurement slot : pl.getParkingDetails().getCoverCars()) {
			if (slot.getHeight().setScale(2, RoundingMode.UP).doubleValue() >= PARKING_SLOT_HEIGHT
					&& slot.getWidth().setScale(2, RoundingMode.UP).doubleValue() >= PARKING_SLOT_WIDTH)
				coverSuccess++;
			else
				coverFailedCount++;
		}
		pl.getParkingDetails().setValidCoverCarSlots(coverParkCount - coverFailedCount);
		if (coverParkCount > 0)
			if (coverFailedCount > 0) {
				if (helper.totalRequiredCarParking.intValue() == pl.getParkingDetails().getValidCoverCarSlots()) {
					setReportOutputDetails(pl, SUB_RULE_40, COVER_PARKING_DIM_DESC,
							PARKING_MIN_AREA + MIN_AREA_EACH_CAR_PARKING,
							OUT_OF + coverParkCount + PARKING + coverFailedCount + PARKING_VIOLATED_MINIMUM_AREA,
							Result.Accepted.getResultVal());
				} else {
					setReportOutputDetails(pl, SUB_RULE_40, COVER_PARKING_DIM_DESC,
							PARKING_MIN_AREA + MIN_AREA_EACH_CAR_PARKING,
							OUT_OF + coverParkCount + PARKING + coverFailedCount + PARKING_VIOLATED_MINIMUM_AREA,
							Result.Not_Accepted.getResultVal());
				}
			} else {
				setReportOutputDetails(pl, SUB_RULE_40, COVER_PARKING_DIM_DESC,
						PARKING_MIN_AREA + MIN_AREA_EACH_CAR_PARKING, NO_VIOLATION_OF_AREA + coverParkCount + PARKING,
						Result.Accepted.getResultVal());
			}

		// Validate dimension of basement
		int bsmntParkCount = pl.getParkingDetails().getBasementCars().size();
		int bsmntFailedCount = 0;
		int bsmntSuccess = 0;
		for (Measurement slot : pl.getParkingDetails().getBasementCars()) {
			if (slot.getHeight().setScale(2, RoundingMode.UP).doubleValue() >= PARKING_SLOT_HEIGHT
					&& slot.getWidth().setScale(2, RoundingMode.UP).doubleValue() >= PARKING_SLOT_WIDTH)
				bsmntSuccess++;
			else
				bsmntFailedCount++;
		}
		pl.getParkingDetails().setValidBasementCarSlots(bsmntParkCount - bsmntFailedCount);
		if (bsmntParkCount > 0)
			if (bsmntFailedCount > 0) {
				if (helper.totalRequiredCarParking.intValue() == pl.getParkingDetails().getValidBasementCarSlots()) {
					setReportOutputDetails(pl, SUB_RULE_40, BSMNT_PARKING_DIM_DESC,
							PARKING_MIN_AREA + MIN_AREA_EACH_CAR_PARKING,
							OUT_OF + bsmntParkCount + PARKING + bsmntFailedCount + PARKING_VIOLATED_MINIMUM_AREA,
							Result.Accepted.getResultVal());
				} else {
					setReportOutputDetails(pl, SUB_RULE_40, BSMNT_PARKING_DIM_DESC,
							PARKING_MIN_AREA + MIN_AREA_EACH_CAR_PARKING,
							OUT_OF + bsmntParkCount + PARKING + bsmntFailedCount + PARKING_VIOLATED_MINIMUM_AREA,
							Result.Not_Accepted.getResultVal());
				}
			} else {
				setReportOutputDetails(pl, SUB_RULE_40, BSMNT_PARKING_DIM_DESC,
						PARKING_MIN_AREA + MIN_AREA_EACH_CAR_PARKING, NO_VIOLATION_OF_AREA + bsmntParkCount + PARKING,
						Result.Accepted.getResultVal());
			}

	}

	private void checkDimensionForSpecialParking(Plan pl, ParkingHelper helper) {

		int success = 0;
		int specialFailedCount = 0;
		int specialParkCount = pl.getParkingDetails().getSpecial().size();
		for (Measurement spParkSlot : pl.getParkingDetails().getSpecial()) {
			if (spParkSlot.getMinimumSide().doubleValue() >= SP_PARK_SLOT_MIN_SIDE)
				success++;
			else
				specialFailedCount++;
		}
		pl.getParkingDetails().setValidSpecialSlots(specialParkCount - specialFailedCount);
		if (specialParkCount > 0)
			if (specialFailedCount > 0) {
				if (helper.daParking.intValue() == pl.getParkingDetails().getValidSpecialSlots()) {
					setReportOutputDetails(pl, SUB_RULE_40_8, SP_PARKING_SLOT_AREA,
							DA_PARKING_MIN_AREA + MINIMUM_AREA_OF_EACH_DA_PARKING,
							NO_VIOLATION_OF_AREA + pl.getParkingDetails().getValidSpecialSlots() + PARKING,
							Result.Accepted.getResultVal());
				} else {
					setReportOutputDetails(pl, SUB_RULE_40_8, SP_PARKING_SLOT_AREA,
							DA_PARKING_MIN_AREA + MINIMUM_AREA_OF_EACH_DA_PARKING,
							OUT_OF + specialParkCount + PARKING + specialFailedCount + PARKING_VIOLATED_MINIMUM_AREA,
							Result.Not_Accepted.getResultVal());
				}
			} else {
				setReportOutputDetails(pl, SUB_RULE_40_8, SP_PARKING_SLOT_AREA,
						DA_PARKING_MIN_AREA + MINIMUM_AREA_OF_EACH_DA_PARKING,
						NO_VIOLATION_OF_AREA + specialParkCount + PARKING, Result.Accepted.getResultVal());
			}
	}

	private void checkDimensionForTwoWheelerParking(Plan pl, ParkingHelper helper) {
		double providedArea = 0;
		int twoWheelParkingCount = pl.getParkingDetails().getTwoWheelers().size();
		int failedTwoWheelCount = 0;
		helper.twoWheelerParking = BigDecimal.valueOf(0.25 * helper.totalRequiredCarParking * 2.70 * 5.50)
				.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
		if (!pl.getParkingDetails().getTwoWheelers().isEmpty()) {
			for (Measurement m : pl.getParkingDetails().getTwoWheelers()) {
				if (m.getWidth().setScale(2, RoundingMode.UP).doubleValue() < TWO_WHEEL_PARKING_AREA_WIDTH
						|| m.getHeight().setScale(2, RoundingMode.UP).doubleValue() < TWO_WHEEL_PARKING_AREA_HEIGHT)
					failedTwoWheelCount++;

				providedArea = providedArea + m.getArea().doubleValue();
			}
		}

		if (providedArea < helper.twoWheelerParking) {
			setReportOutputDetails(pl, SUB_RULE_34_2, TWO_WHEELER_PARK_AREA,
					helper.twoWheelerParking + " " + DcrConstants.SQMTRS,
					BigDecimal.valueOf(providedArea).setScale(2, BigDecimal.ROUND_HALF_UP) + " " + DcrConstants.SQMTRS,
					Result.Not_Accepted.getResultVal());
		} else {
			setReportOutputDetails(pl, SUB_RULE_34_2, TWO_WHEELER_PARK_AREA,
					helper.twoWheelerParking + " " + DcrConstants.SQMTRS,
					BigDecimal.valueOf(providedArea).setScale(2, BigDecimal.ROUND_HALF_UP) + " " + DcrConstants.SQMTRS,
					Result.Accepted.getResultVal());
		}

		if (providedArea >= helper.twoWheelerParking && failedTwoWheelCount >= 0) {
			setReportOutputDetails(pl, SUB_RULE_40, TWO_WHEELER_DIM_DESC, PARKING_AREA_DIM,
					OUT_OF + twoWheelParkingCount + PARKING + failedTwoWheelCount + PARKING_VIOLATED_DIM,
					Result.Accepted.getResultVal());
		} else {
			setReportOutputDetails(pl, SUB_RULE_40, TWO_WHEELER_DIM_DESC, PARKING_AREA_DIM,
					OUT_OF + twoWheelParkingCount + PARKING + failedTwoWheelCount + PARKING_VIOLATED_DIM,
					Result.Not_Accepted.getResultVal());
		}
	}

	private BigDecimal getTotalCarpetAreaByOccupancy(Plan pl, OccupancyType type) {
		BigDecimal totalArea = BigDecimal.ZERO;
		for (Block b : pl.getBlocks())
			for (Occupancy occupancy : b.getBuilding().getTotalArea())
				if (occupancy.getType().equals(type))
					totalArea = totalArea.add(occupancy.getCarpetArea());
		return totalArea;
	}

	private void checkAreaForLoadUnloadSpaces(Plan pl) {
		double providedArea = 0;
		BigDecimal totalBuiltupArea = pl.getOccupancies().stream().map(Occupancy::getBuiltUpArea)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		double requiredArea = Math.abs(((totalBuiltupArea.doubleValue() - 700) / 1000) * 30);
		if (!pl.getParkingDetails().getLoadUnload().isEmpty()) {
			for (Measurement m : pl.getParkingDetails().getLoadUnload()) {
				if (m.getArea().compareTo(BigDecimal.valueOf(30)) >= 0)
					providedArea = providedArea + m.getArea().doubleValue();
			}
		}
		if (providedArea < requiredArea) {
			setReportOutputDetails(pl, SUB_RULE_40, LOADING_UNLOADING_AREA, requiredArea + " " + DcrConstants.SQMTRS,
					BigDecimal.valueOf(providedArea).setScale(2, BigDecimal.ROUND_HALF_UP) + " " + DcrConstants.SQMTRS,
					Result.Not_Accepted.getResultVal());
		} else {
			setReportOutputDetails(pl, SUB_RULE_40, LOADING_UNLOADING_AREA, requiredArea + " " + DcrConstants.SQMTRS,
					BigDecimal.valueOf(providedArea).setScale(2, BigDecimal.ROUND_HALF_UP) + " " + DcrConstants.SQMTRS,
					Result.Accepted.getResultVal());
		}
	}

	@Override
	public Map<String, Date> getAmendments() {
		return new LinkedHashMap<>();
	}
}
