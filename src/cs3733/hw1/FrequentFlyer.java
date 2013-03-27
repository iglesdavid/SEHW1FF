/*******************************************************************************
 * Copyright (c) 2012 Gary F. Pollice
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: gpollice
 *******************************************************************************/

package cs3733.hw1;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import cs3733.hw1.InsufficientPointsException;

/**
 * Description
 * 
 * @author gpollice
 * @version Mar 19, 2013
 */
@SuppressWarnings("unused")
public class FrequentFlyer {
	private String frequentFlyerId;
	private int points;
	public ArrayList<FFTransaction> history;

	/**
	 * Constructor. The only identification for a frequent flyer is the ID,
	 * which will be used by clients for various purposes.
	 * 
	 * @param frequentFlyerId
	 *            the frequent flyer ID
	 */
	public FrequentFlyer(String frequentFlyerId) {
		this.frequentFlyerId = frequentFlyerId;
		this.history = new ArrayList<FFTransaction>();
	}

	/**
	 * Record the completion of a flight for the frequent flyer. This should
	 * update the flyer's level and points appropriately. You can assume that
	 * the airport codes are valid.
	 * 
	 * This should also create an FFTransaction for this flyer and add it to the
	 * end of the transaction history.
	 * 
	 * @param from
	 *            the source airport's code
	 * @param to
	 *            the destination airport's code
	 * @return the frequent flyer's point level after this flight (truncating
	 *         any fractions).
	 * @throws InsufficientPointsException
	 */
	public int recordFlight(String from, String to)
			throws InsufficientPointsException {

		FrequentFlyerLevel level = getFrequentFlyerLevel();
		double transactionAmount = DistanceTable.getInstance().getDistance(
				from, to);

		if (level == FrequentFlyerLevel.SILVER) {
			transactionAmount = transactionAmount * 1.25;
		} else if (level == FrequentFlyerLevel.GOLD) {
			transactionAmount = transactionAmount * 1.5;
		} else if (level == FrequentFlyerLevel.PLATINUM) {
			transactionAmount = transactionAmount * 2.0;
		} else
			transactionAmount = transactionAmount * 1.0;

		FFTransaction flight = new FFTransaction(from, to, transactionAmount);

		history.add(flight);
		//(flight, (flight.getTransactionNumber();
		//(flight.getTransactionNumber(), flight);
		return (int) flight.getTransactionAmount();
	}

	/**
	 * Redeem points to pay for a flight. As long as there are enough points in
	 * the account to cover the cost of the flight (10 points per mile), the
	 * points are removed from the flyer's available total.
	 * 
	 * This should also create an FFTransaction for this flyer and add it to the
	 * end of the transaction history.
	 * 
	 * @param from
	 *            the source airport's code
	 * @param to
	 *            the destination airport's code
	 * @returnthe frequent flyer's point level after this redemption (truncating
	 *            any fractions).
	 * @throws InsufficientPointsException
	 *             if there not enough points to pay for the flight
	 */
	public int redeemPoints(String from, String to)
			throws InsufficientPointsException {
		double distance = DistanceTable.getInstance().getDistance(from, to);
		double points = getPointsAvailable() - distance;
		FFTransaction flight = new FFTransaction(from, to, points);
		return (int) flight.getTransactionAmount();
	}

	/**
	 * This method is used by the airline to adjust the frequent flyer's points
	 * This can be done to compensate the flyer for inconviences, award bonuses,
	 * or make any other adjustment.
	 * 
	 * This will also add a FFTransaction to the flyer's history. The
	 * transaction will have a <code>from</code> string of "ADJUST" and a null
	 * <code>to</code> string.
	 * 
	 * @param adjustment
	 *            the amount to be added to the points (can be negative)
	 * @return the resulting points available for this flyer, truncating any
	 *         fractional points.
	 */
	public int adjustBalance(int adjustment) {
		points += adjustment;
		
		FFTransaction flight = new FFTransaction("ADJUST", null, adjustment);
		history.add(flight);
		
		return points;
	}

	/**
	 * This method is used by the airline to adjust the number of miles flown by
	 * the frequent flyer. It's similar to adjustBalance, but it adjusts the
	 * miles rather than the points. This does, however, adjust the points (and
	 * possibly the level) appropriately.
	 * 
	 * @param the
	 *            mileage adjustment
	 * @return the resulting points (not miles) available for this flyer,
	 *         truncating any fractional points.
	 * @throws InsufficientPointsException 
	 */
	public int adjustMilesFlown(int adjustment) throws InsufficientPointsException {
		
		
		int mileage = points + adjustBalance(adjustment);
		
		return mileage;
		
		
	}

	/**
	 * Return an iterator to the transaction history. This should return an
	 * iterator to the collection of FFTransactions for this frequent flyer. The
	 * iterator should return the transactions in the order in which they were
	 * added to the flyer's history (that is the oldest transaction first).
	 * 
	 * @return
	 */
	public Iterator<FFTransaction> getTransactionHistory() {
		// TODO
		return null;
	}

	/**
	 * @return the points available for this flyer, truncating any fractional
	 *         points.
	 */
	public int getPointsAvailable() {
		return points;
	}

	/**
	 * @return this frequent flyer's current level
	 */
	public FrequentFlyerLevel getFrequentFlyerLevel()
			throws InsufficientPointsException {

		if (getPointsAvailable() >= 100000) {
			return FrequentFlyerLevel.PLATINUM;
		} else if (getPointsAvailable() > 50000
				&& getPointsAvailable() < 100000) {
			return FrequentFlyerLevel.GOLD;
		} else if (getPointsAvailable() > 25000 && getPointsAvailable() < 50000) {
			return FrequentFlyerLevel.SILVER;
		} else if (getPointsAvailable() >= 0) {
			return FrequentFlyerLevel.BASIC;
		} else
			throw new InsufficientPointsException("Hurr Durr");
	}

	/**
	 * @param frequentFlyerId
	 * @return the frequent flyer ID
	 */
	public String getFrequentFlyerId() {
		return frequentFlyerId;
	}
}
