package com.comerbien.backend.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "weekly_menus")
public class WeeklyMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private LocalDate weekStartDate;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "weeklyMenu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DayMeal> dayMeals = new ArrayList<>();

    // Constructores
    public WeeklyMenu() {
    }

    public WeeklyMenu(LocalDate weekStartDate, User user) {
        this.weekStartDate = weekStartDate;
        this.user = user;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getWeekStartDate() {
        return weekStartDate;
    }

    public void setWeekStartDate(LocalDate weekStartDate) {
        this.weekStartDate = weekStartDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<DayMeal> getDayMeals() {
        return dayMeals;
    }

    public void setDayMeals(List<DayMeal> dayMeals) {
        this.dayMeals = dayMeals;
    }
}
