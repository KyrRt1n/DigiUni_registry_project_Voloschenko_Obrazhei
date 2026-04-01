package ua.sopsany.dto;

public record FacultyStatsRecord(
        String facultyName,
        long totalStudents,
        long budgetStudents,
        long contractStudents
) {

    public FacultyStatsRecord {
        if (totalStudents < 0) {
            throw new IllegalArgumentException("Кількість студентів не може бути від'ємною");
        }
    }

    public double budgetPercent() {
        if (totalStudents == 0) return 0.0;
        return (double) budgetStudents / totalStudents * 100;
    }

    public double contractPercent() {
        if (totalStudents == 0) return 0.0;
        return (double) contractStudents / totalStudents * 100;
    }


    public String toReportLine() {
        return String.format("%-30s | Всього: %3d | Бюджет: %3d (%.1f%%) | Контракт: %3d (%.1f%%)",
                facultyName(), totalStudents(), budgetStudents(), budgetPercent(),
                contractStudents(), contractPercent());
    }
}