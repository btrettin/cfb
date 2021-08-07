package com.example.cfbstats.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Game {
    private Integer id	;
    private Integer season	;
    private Integer week	;
    private String season_type	;
    private String start_date	;
    private Boolean start_time_tbd	;
    private Boolean neutral_site	;
    private Boolean conference_game	;
    private Integer attendance	;
    private Integer venue_id	;
    private String venue	;
    private Integer home_id;
    private String home_team	;
    private String home_conference	;
    private Integer home_points	;
    private List<Integer> home_line_scores	;
    private Double home_post_win_prob	;
    private Integer away_id	;
    private String away_team	;
    private String away_conference;
    private Integer away_points	;
    private List<Integer> away_line_scores;
    private Double away_post_win_prob;
    private Double excitement_index	;
    private String highlights;
    private String notes	;
}
