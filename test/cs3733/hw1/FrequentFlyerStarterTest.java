/*******************************************************************************
 * Copyright (c) 2012 Gary F. Pollice
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    gpollice
 *******************************************************************************/

package cs3733.hw1;

import static org.junit.Assert.*;
import java.util.Iterator;
import org.junit.*;

/**
 * Description
 *
 * @author gpollice
 * @version Mar 19, 2013
 */
public class FrequentFlyerStarterTest
{
	private FrequentFlyer flyer1, flyer2;

	/**
	 * Put some things into the distanceTable;
	 */
	@BeforeClass
	public static void setupContext()
	{
		final DistanceTable dt = DistanceTable.getInstance();
		dt.clear();
		dt.addAirportPair("BOS", "JFK", 187);
		dt.addAirportPair("EWR", "BOS", 200);
		dt.addAirportPair("ATL", "BOS", 946);
		dt.addAirportPair("JFK", "SFO", 2578);
		dt.addAirportPair("DEN", "SEA", 1022);
		dt.addAirportPair("ORD", "ORD", 885);
		dt.addAirportPair("ORD", "BOS", 864);
	}
	
	/**
	 * Create frequent flyers for the test.
	 *
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		flyer1 = new FrequentFlyer("F0001");
		flyer2 = new FrequentFlyer("F0002");
		FFTransaction.resetTransactionNumbers();
	}

	@Test
	public void idIsCorrect()
	{
		assertEquals("F0001", flyer1.getFrequentFlyerId());
	}
	
	@Test
	public void basicMemberFlightUpdatesPoints() throws InsufficientPointsException
	{
		assertEquals(2578, flyer1.recordFlight("JFK", "SFO"));
	}

	@Test
	public void initialLevelIsBasic() throws InsufficientPointsException
	{
		assertEquals(FrequentFlyerLevel.BASIC, flyer2.getFrequentFlyerLevel());
	}
	
	@Test
	public void enterFlightChangingLevels() throws InsufficientPointsException
	{
		flyer1.adjustMilesFlown(24900);
		flyer1.recordFlight("BOS", "EWR");
		assertEquals(FrequentFlyerLevel.SILVER, flyer1.getFrequentFlyerLevel());
		assertEquals(25125, flyer1.getPointsAvailable());
	}
	
	@Test
	public void historyTransactionIsCorrect() throws InsufficientPointsException
	{
		flyer1.recordFlight("BOS", "EWR");
		Iterator<FFTransaction> history = flyer1.getTransactionHistory();
		assertTrue(history.hasNext());
		FFTransaction fft = history.next();
		assertEquals("BOS", fft.getFrom());
		assertEquals("EWR", fft.getTo());
		assertEquals(1, fft.getTransactionNumber());
		assertEquals(200.0, fft.getTransactionAmount(), 0.0001);
	}
}
