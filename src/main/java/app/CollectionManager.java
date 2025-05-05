package app;

import commands.*;
import modules.*;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

import java.io.IOException;
import java.util.PriorityQueue;
import java.util.Queue;
import java.io.*;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.Collectors;

public class CollectionManager {
    private Queue<Movie> movies = new PriorityQueue<>();
    private Integer nextId = 1;
    private static final String FILE_NAME = "/Users/macbook/Desktop/лабы/Java/Lab6/src/main/resources/collection.csv";
    private LocalDateTime initializationDate;

    public CollectionManager() {
        this.initializationDate = LocalDateTime.now();
        load();
    }

    public String add(Movie movie) {
        if (isPassportIdUnique(movie.getDirector().getPassportID())) {
            movie.setId(getNextId());
            movies.add(movie);
            return "Movie added successfully.";
        } else {
            return "PassportID must be unique. Movie not added.";
        }
    }

    private boolean isPassportIdUnique(String passportID) {
        for (Movie movie : movies) {
            if (movie.getDirector().getPassportID().equals(passportID)) {
                return false;
            }
        }
        return true;
    }

    public Integer getNextId() {
        return nextId++;
    }


    public String show() {
        if (movies.isEmpty()) {
            return "Collection is empty.";
        } else {
            StringBuilder sb = new StringBuilder();
            for (Movie movie : movies.stream().sorted((m1, m2) -> m1.getId().compareTo(m2.getId())).collect(Collectors.toList())) {
                sb.append(movie.toString()).append("\n");
            }
            return "Collection:\n" + sb.toString();
        }
    }

    public String removeById(int id) {
        boolean removed = movies.removeIf(movie -> movie.getId() == id);
        return removed ? "Movie with id " + id + " removed successfully." : "Movie with id " + id + " not found.";
    }

    public String clear() {
        movies.clear();
        return "Collection cleared successfully.";
    }

    public String save() {
        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            writer.append("id,name,coordinates_x,coordinates_y,creationDate,oscarsCount,length,genre,mpaaRating,director_name,director_passportID,director_location_x,director_location_y,director_location_name\n");
            for (Movie movie : movies) {
                writer.append(movieToCsv(movie)).append("\n");
            }
            return "Collection saved successfully.";
        } catch (IOException e) {
            return "Error saving collection: " + e.getMessage();
        }
    }

    private String movieToCsv(Movie movie) {
        StringBuilder sb = new StringBuilder();
        sb.append(movie.getId()).append(",");
        sb.append(movie.getName()).append(",");
        sb.append(movie.getCoordinates().getX()).append(",");
        sb.append(movie.getCoordinates().getY()).append(",");
        sb.append(movie.getCreationDate()).append(",");
        sb.append(movie.getOscarsCount()).append(",");
        sb.append(movie.getLength()).append(",");
        sb.append(movie.getGenre()).append(",");
        sb.append(movie.getMpaaRating()).append(",");
        sb.append(movie.getDirector().getName()).append(",");
        sb.append(movie.getDirector().getPassportID()).append(",");
        if (movie.getDirector().getLocation() != null) {
            sb.append(movie.getDirector().getLocation().getX()).append(",");
            sb.append(movie.getDirector().getLocation().getY()).append(",");
            sb.append(movie.getDirector().getLocation().getName());
        } else {
            sb.append(",,");
        }
        return sb.toString();
    }

    public String update(int id, Movie updatedMovie) {
        boolean updated = movies.stream()
                .filter(movie -> movie.getId() == id && isPassportIdUnique(updatedMovie.getDirector().getPassportID()))
                .findFirst()
                .map(movie -> {
                    updatedMovie.setId(id);
                    movies.remove(movie);
                    movies.add(updatedMovie);
                    return true;
                })
                .orElse(false);
        return updated ? "Movie with id " + id + " updated successfully." : "Movie with id " + id + " not found or passportID is not unique.";
    }


    public String removeHead() {
        if (movies.isEmpty()) {
            return "Collection is empty.";
        } else {
            Movie firstMovie = movies.poll();
            return "First movie removed successfully: " + firstMovie.toString();
        }
    }

    public String countGreaterThanGenre(MovieGenre genre) {
        long count = movies.stream()
                .filter(movie -> movie.getGenre() != null && movie.getGenre().compareTo(genre) > 0)
                .count();
        return "Number of movies with genre greater than " + genre + ": " + count;
    }


    public String addIfMin(Movie movie) {
        if ((movies.isEmpty() || movie.compareTo(movies.peek()) < 0) && isPassportIdUnique(movie.getDirector().getPassportID())) {
            movie.setId(getNextId());
            movies.add(movie);
            return "Movie added successfully.";
        } else {
            return "Movie is not the smallest or passportId is not unique. Not added.";
        }
    }



    public String removeGreater(Movie movie) {
        int removedCount = 0;
        for (Movie m : movies) {
            if (m.compareTo(movie) > 0) {
                movies.remove(m);
                removedCount++;
            }
        }
        return "Removed " + removedCount + " movies greater than the specified movie.";
    }

    public String getInfo() {
        return "Collection type: " + movies.getClass().getName() + "\n" +
                "Initialization date: " + initializationDate + "\n" +
                "Number of elements: " + movies.size();
    }

    public String minByCoordinates() {
        if (movies.isEmpty()) {
            return "Collection is empty.";
        } else {
            Movie minMovie = movies.stream()
                    .min((m1, m2) -> m1.getCoordinates().compareTo(m2.getCoordinates()))
                    .orElse(null);
            return minMovie != null ? minMovie.toString() : "No movie found.";
        }
    }

    public String maxById() {
        if (movies.isEmpty()) {
            return "Collection is empty.";
        } else {
            Movie maxMovie = movies.stream()
                    .max((m1, m2) -> Integer.compare(m1.getId(), m2.getId()))
                    .orElse(null);
            return maxMovie != null ? maxMovie.toString() : "No movie found.";
        }
    }

    public String executeScript(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            StringBuilder result = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                String[] commandParts = line.split(" ", 2);
                String commandName = commandParts[0];
                String commandArgs = commandParts.length > 1 ? commandParts[1] : "";

                Command command = null;
                switch (commandName) {
                    case "add":
                        Movie movie = MovieFactory.createMovie();
                        command = new AddCommand(new AddCommandArgs(movie));
                        break;
                    case "show":
                        command = new ShowCommand();
                        break;
                    case "remove_by_id":
                        try {
                            int id = Integer.parseInt(commandArgs);
                            command = new RemoveByIdCommand(id);
                        } catch (NumberFormatException e) {
                            result.append("Invalid ID format. Please enter a valid number.\n");
                            continue;
                        }
                        break;
                    case "update":
                        try {
                            String[] updateParts = commandArgs.split(" ", 2);
                            int updateId = Integer.parseInt(updateParts[0]);
                            Movie updatedMovie = MovieFactory.createMovie();
                            command = new UpdateCommand(updateId, updatedMovie);
                        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                            result.append("Invalid command format. Please use 'update id'.\n");
                            continue;
                        }
                        break;
                    case "remove_head":
                        command = new RemoveHeadCommand();
                        break;
                    case "clear":
                        command = new ClearCommand();
                        break;
                    case "execute_script":
                        command = new ExecuteScriptCommand(commandArgs);
                        break;
                    case "count_greater_than_genre":
                        try {
                            MovieGenre genre = MovieGenre.valueOf(commandArgs.toUpperCase());
                            command = new CountGreaterThanGenreCommand(genre);
                        } catch (IllegalArgumentException e) {
                            result.append("Invalid genre. Please enter a valid genre.\n");
                            continue;
                        }
                        break;
                    case "add_if_min":
                        Movie newMovie = MovieFactory.createMovie();
                        command = new AddIfMinCommand(newMovie);
                        break;
                    case "remove_greater":
                        Movie greaterMovie = MovieFactory.createMovie();
                        command = new RemoveGreaterCommand(greaterMovie);
                        break;
                    case "min_by_coordinates":
                        command = new MinByCoordinatesCommand();
                        break;
                    case "max_by_id":
                        command = new MaxByIdCommand();
                        break;
                    case "help":
                        command = new HelpCommand();
                        break;
                    case "info":
                        command = new InfoCommand();
                        break;
                    case "exit":
                        command = new ExitCommand();
                        break;
                    default:
                        result.append("Unknown command.\n");
                        continue;
                }

                if (command != null) {
                    result.append(command.execute(this, null)).append("\n");
                }
            }
            return result.toString();
        } catch (IOException e) {
            return "Error executing script: " + e.getMessage();
        }
    }

    public Queue<Movie> getMovies() {
        return movies;
    }

    public boolean checkIdExists(int id) {
        for (Movie movie : movies) {
            if (movie.getId() == id) {
                return true;
            }
        }
        return false;
    }

    private void load() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("File not found. Starting with an empty collection.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // Пропускаем заголовок
                }
                Movie movie = csvToMovie(line);
                if (movie != null) {
                    movies.add(movie);
                    if (movie.getId() >= nextId) {
                        nextId = movie.getId() + 1;
                    }
                }
            }
            System.out.println("Collection loaded successfully. Total movies: " + movies.size());
        } catch (IOException e) {
            System.err.println("Error loading collection: " + e.getMessage());
        }
    }



    private Movie csvToMovie(String csvLine) {
        String[] values = csvLine.split(",");
        try {
            Movie movie= new Movie();
            movie.setId((Integer.parseInt(values[0])));
            movie.setName(values[1]);

            double coordinatesX = Double.parseDouble(values[2]);
            long coordinatesY = Long.parseLong(values[3]);
            movie.setCoordinates(new Coordinates(coordinatesX, coordinatesY));

            movie.setCreationDate(LocalDateTime.parse(values[4]));
            movie.setOscarsCount(Integer.parseInt(values[5]));
            movie.setLength(Integer.parseInt(values[6]));

            try{movie.setGenre(MovieGenre.valueOf(values[7]));} catch (Exception e){};
            try{movie.setMpaaRating(MpaaRating.valueOf(values[8]));} catch (Exception e){};

            String locationName;
            if (values.length < 14) {
                locationName = "";
            } else {
                locationName = values[13];
            }

            movie.setDirector(new Person(values[9], values[10], new Location(Long.parseLong(values[11]),
                    Double.parseDouble(values[12]), locationName)));

            return movie;
        } catch (IllegalArgumentException e) {
            System.err.println("Error parsing movie from CSV: " + e.getMessage());
            return null;
        }
    }
}
