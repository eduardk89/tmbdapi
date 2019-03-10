package com.ratiose.testtask.service.tmdb.pojo;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieObject
{
    @JsonProperty("vote_count")
    private Integer voteCount;
    private Integer id;
    @JsonProperty("vote_average")
    private BigDecimal voteAverage;
    private String title;
    private BigDecimal popularity;
    @JsonProperty("original_language")
    private String originalLanguage;
    @JsonProperty("original_title")
    private String originalTitle;
    @JsonProperty("genre_ids")
    private List<Integer> genreIds;
    private Boolean adult;
    private String overview;
    @JsonProperty("release_date")
    private String releaseDate;

    public Integer getVoteCount()
    {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount)
    {
        this.voteCount = voteCount;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public BigDecimal getVoteAverage()
    {
        return voteAverage;
    }

    public void setVoteAverage(BigDecimal voteAverage)
    {
        this.voteAverage = voteAverage;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public BigDecimal getPopularity()
    {
        return popularity;
    }

    public void setPopularity(BigDecimal popularity)
    {
        this.popularity = popularity;
    }

    public String getOriginalLanguage()
    {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage)
    {
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalTitle()
    {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle)
    {
        this.originalTitle = originalTitle;
    }

    public List<Integer> getGenreIds()
    {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds)
    {
        this.genreIds = genreIds;
    }

    public Boolean getAdult()
    {
        return adult;
    }

    public void setAdult(Boolean adult)
    {
        this.adult = adult;
    }

    public String getOverview()
    {
        return overview;
    }

    public void setOverview(String overview)
    {
        this.overview = overview;
    }

    public String getReleaseDate()
    {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate)
    {
        this.releaseDate = releaseDate;
    }
}
