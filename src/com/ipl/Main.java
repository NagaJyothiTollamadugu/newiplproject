package com.ipl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {

    public static final int MATCH_ID = 0;
    public static final int SEASON = 1;
    public static final int CITY = 2;
    public static final int DATE = 3;
    public static final int TEAM1 = 4;
    public static final int TEAM2 = 5;
    public static final int TOSS_WINNER = 6;
    public static final int TOSS_DECISSION = 7;
    public static final int RESULT = 8;
    public static final int DL_APPLIED = 9;
    public static final int WINNER = 10;
    public static final int WIN_BY_RUNS = 11;
    public static final int WIN_BY_WICKETS = 12;
    public static final int PLYAER_OF_MATCH = 13;
    public static final int VENUE = 14;


    public static void main(String[] args) {
        List<Match> matches = getMatchesData();
        List<Deliver> deliveries = getDeliveriesData();

        findNumberOfMatchesPlayedPerTeam(matches);
        findNumberOfMatchesWonPerTeamInAllSeasons(matches);
        findExtrarunsConcededPerTeamIn2016(matches, deliveries);
        findMostEconomicalBowlerIn2015(matches, deliveries);
       findNumberOfMatchesHappenedInaEachCityOverTheEntireSeasons(matches);

    }

    private static List<Deliver> getDeliveriesData() {
        List<Deliver> deliveries = new ArrayList<>();

        String deliverFilePath = "/home/jyothi/Downloads/archive (1)/deliveries.csv";

        BufferedReader reader = null;
        String line = "";

        try {
            reader = new BufferedReader(new FileReader(deliverFilePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int flag = 0;

        try {
            while ((line = reader.readLine()) != null) {
                if (flag == 0) {
                    flag = 1;
                    continue;
                }

                String[] fields = line.split(",");

                Deliver delivery = new Deliver();

                delivery.setDeliveryMatchId(fields[0]);
                delivery.setInnings(fields[1]);
                delivery.setBattingTeam(fields[2]);
                delivery.setBowlingTeam(fields[3]);
                delivery.setOver(fields[4]);
                delivery.setBall(fields[5]);
                delivery.setBatsman(fields[6]);
                delivery.setNonStriker(fields[7]);
                delivery.setBowler(fields[8]);
                delivery.setIsSuperOver(fields[9]);
                delivery.setWideRuns(fields[10]);
                delivery.setByRuns(fields[11]);
                delivery.setLegByRuns(fields[12]);
                delivery.setNoBall(fields[13]);
                delivery.setPenaltyRuns(fields[14]);
                delivery.setBatsmanRuns(fields[15]);
                delivery.setExtraRuns(fields[16]);
                delivery.setTotalRuns(fields[17]);
                deliveries.add(delivery);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return deliveries;
    }

    private static List<Match> getMatchesData() {
        List<Match> matches = new ArrayList<>();

        String matchFilePath = "/home/jyothi/Downloads/archive (1)/matches.csv";

        BufferedReader reader = null;
        String line = "";
        int flag = 0;

        try {
            reader = new BufferedReader(new FileReader(matchFilePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            while ((line = reader.readLine()) != null) {
                if (flag == 0) {
                    flag = 1;
                    continue;
                }
                String[] fields = line.split(",");

                Match match = new Match();
                match.setMatchId(fields[MATCH_ID]);
                match.setSeason(fields[SEASON]);
                match.setCity(fields[CITY]);
                match.setDate(fields[DATE]);
                match.setTeam1(fields[TEAM1]);
                match.setTeam2(fields[TEAM2]);
                match.setTossWinner(fields[TOSS_WINNER]);
                match.setTossDecission(fields[TOSS_DECISSION]);
                match.setResult(fields[RESULT]);
                match.setDlAppalied(fields[DL_APPLIED]);
                match.setWinner(fields[WINNER]);
                match.setWinByRuns(fields[WIN_BY_RUNS]);
                match.setWinByWickes(fields[WIN_BY_WICKETS]);
                match.setPlayerOfMatch(fields[PLYAER_OF_MATCH]);
                match.setVenue(fields[VENUE]);
                matches.add(match);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return matches;
    }

    private static void findNumberOfMatchesPlayedPerTeam(List<Match> matches) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 2008; i < 2018; i++)
            map.put(i, 0);
        for (int i = 0; i < matches.size(); i++) {
            int yr = Integer.valueOf(matches.get(i).getSeason());
            map.put(yr, map.get(yr) + 1);
        }

        System.out.println("***Displaying the Count of matches held in each year****");
        System.out.println("Year : Matches");
        for (Map.Entry<Integer, Integer> entry : map.entrySet())
            System.out.println(entry.getKey() + " : " + entry.getValue());

    }

    private static void findNumberOfMatchesWonPerTeamInAllSeasons(List<Match> matches) {
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < matches.size(); i++) {
            String team = matches.get(i).getWinner();
            int idx = map.get(team) != null ? map.get(team) : 0;
            map.put(team, idx + 1);
        }
        System.out.println("***Displaying the Count of matches won by each team over all years****");
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (!entry.getKey().equals(""))
                System.out.println(entry.getKey() + " : " + entry.getValue());
        }

    }

    private static void findExtrarunsConcededPerTeamIn2016(List<Match> matches, List<Deliver> deliveries) {
        Map<String, Integer> map = new HashMap<>();
        List<Integer> ids2016 = new ArrayList<>();
        for (int i = 0; i < matches.size(); i++) {
            int yr = Integer.valueOf(matches.get(i).getSeason());
            if (yr == 2016)
                ids2016.add(Integer.valueOf(matches.get(i).getMatchId()));
        }
        for (int i = 0; i < deliveries.size(); i++) {
            int matchId = Integer.valueOf(deliveries.get(i).getDeliveryMatchId());
            String team = deliveries.get(i).getBowlingTeam();
            int idx = map.get(team) != null ? map.get(team) : 0;
            int extra = Integer.valueOf(deliveries.get(i).getExtraRuns());
            if (ids2016.contains(matchId)) {
                map.put(team,idx+extra);

            }

        }

        System.out.println("***Displaying the Extra Runs conceded per team in year 2016****");
        for (Map.Entry<String, Integer> entry : map.entrySet())
            System.out.println(entry.getKey() + " : " + entry.getValue());
    }

    private static void findMostEconomicalBowlerIn2015(List<Match> matches, List<Deliver> deliveries) {
        Map<String, Integer> map = new HashMap<>();
        Map<String, Integer> map1 = new HashMap<>();
        List<Integer> ids2015 = new ArrayList<>();
        for (int i = 0; i < matches.size(); i++) {
            int yr = Integer.valueOf(matches.get(i).getSeason());
            if (yr == 2015)
                ids2015.add(Integer.valueOf(deliveries.get(i).getDeliveryMatchId()));
        }
        for (int i = 0; i < deliveries.size(); i++) {
            int matchId = Integer.valueOf(deliveries.get(i).getDeliveryMatchId());
            String team = deliveries.get(i).getBowlingTeam();
            int idx = map.get(team) != null ? map.get(team) : 0;
            int extra = Integer.valueOf(deliveries.get(i).getTotalRuns());
            if (ids2015.contains(matchId)) {
                String bowler = deliveries.get(i).getBowler();
                int runs = Integer.valueOf(deliveries.get(i).getTotalRuns());
                int idx1 = map.get(bowler) != null ? map.get(bowler) : 0;
                map.put(bowler, idx1 + 1);
                int idx2 = map1.get(bowler) != null ? map1.get(bowler) : 0;
                map1.put(bowler, idx2 + runs);
            }
        }

        System.out.println("***Displaying the Highest Economy rate Bowlers in 2015****");

        for (Map.Entry<String, Integer> entry : map1.entrySet()) {
            String bowler = entry.getKey();
            for (Map.Entry<String, Integer> entry1 : map.entrySet()) {
                if (entry1.getKey().equals(bowler)) {
                    map1.put(bowler, map1.get(bowler) / entry1.getValue());
                    break;
                }
            }
        }
        for (Map.Entry<String, Integer> entry : map1.entrySet())
            System.out.println(entry.getKey() + " : " + entry.getValue());
    }




   private static void findNumberOfMatchesHappenedInaEachCityOverTheEntireSeasons(List<Match> matches) {
       Map<String, Integer> map = new HashMap<>();

       for (int i = 0; i < matches.size(); i++) {

           if(matches.get(i).getCity()=="") {
               continue;
           }
               if (map.containsKey(matches.get(i).getCity())) {
                   map.put(matches.get(i).getCity(), map.get(matches.get(i).getCity()) + 1);
               } else {
                   map.put(matches.get(i).getCity(), 1);
               }

       }

           System.out.println("***Displaying Matches Happened In a Each City Over The Entire Seasons****");
           for (Map.Entry<String, Integer> entry : map.entrySet())
               System.out.println(entry.getKey() + " : " + entry.getValue());
       }

}