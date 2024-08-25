package com.adventofcode.year2015;

import org.apache.commons.lang3.StringUtils;
import it.unimi.dsi.fastutil.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

public final class Day19 {
    private static final Logger LOGGER = LoggerFactory.getLogger(Day19.class);

    private Day19() {
        // No-Op
    }

    public static List<String> readMolecule(String molecule) {
        List<String> chain = new ArrayList<>();
        char[] chars = molecule.toCharArray();
        StringBuilder currentElement = new StringBuilder();
        for (char aChar : chars) {
            if (Character.isLowerCase(aChar)) {
                currentElement.append(aChar);
            } else if (!currentElement.isEmpty()) {
                chain.add(currentElement.toString());
                currentElement.setLength(0);
                currentElement.append(aChar);
            } else {
                currentElement.append(aChar);
            }
        }
        chain.add(currentElement.toString());
        return chain;
    }

    /**
     * --- Day 19: Medicine for Rudolph ---
     *
     * Rudolph the Red-Nosed Reindeer is sick! His nose isn't shining very
     * brightly, and he needs medicine.
     *
     * Red-Nosed Reindeer biology isn't similar to regular reindeer biology;
     * Rudolph is going to need custom-made medicine. Unfortunately, Red-Nosed
     * Reindeer chemistry isn't similar to regular reindeer chemistry, either.
     *
     * The North Pole is equipped with a Red-Nosed Reindeer nuclear fusion/fission
     * plant, capable of constructing any Red-Nosed Reindeer molecule you need. It
     * works by starting with some input molecule and then doing a series of
     * replacements, one per step, until it has the right molecule.
     *
     * However, the machine has to be calibrated before it can be used.
     * Calibration involves determining the number of molecules that can be
     * generated in one step from a given starting point.
     *
     * For example, imagine a simpler machine that supports only the following
     * replacements:
     *
     * H => HO
     * H => OH
     * O => HH
     *
     * Given the replacements above and starting with HOH, the following molecules
     * could be generated:
     *
     *   - HOOH (via H => HO on the first H).
     *   - HOHO (via H => HO on the second H).
     *   - OHOH (via H => OH on the first H).
     *   - HOOH (via H => OH on the second H).
     *   - HHHH (via O => HH).
     *
     * So, in the example above, there are 4 distinct molecules (not five, because
     * HOOH appears twice) after one replacement from HOH. Santa's favorite
     * molecule, HOHOHO, can become 7 distinct molecules (over nine replacements:
     * six from H, and three from O).
     *
     * The machine replaces without regard for the surrounding characters. For
     * example, given the string H2O, the transition H => OO would result in OO2O.
     *
     * Your puzzle input describes all of the possible replacements and, at the
     * bottom, the medicine molecule for which you need to calibrate the machine.
     * How many distinct molecules can be created after all the different ways you
     * can do one replacement on the medicine molecule?
     *
     * Your puzzle answer was 535.
     */
    public static Set<List<String>> findReplacements(Scanner scanner) {
        Map<String, List<List<String>>> replacements = new HashMap<>();
        List<String> medicineMolecule = null;
        boolean readMolecule = false;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (StringUtils.isBlank(line)) {
                readMolecule = true;
                continue;
            }

            if (!readMolecule) {
                String[] split = line.split(" => ");
                replacements.computeIfAbsent(split[0], ignore -> new ArrayList<>()).add(readMolecule(split[1]));
            } else {
                medicineMolecule = readMolecule(line);
            }
        }

        Objects.requireNonNull(medicineMolecule);

        LOGGER.info("Replacements: {}", replacements);
        LOGGER.info("medicineMolecule: {}", medicineMolecule);
        return findNextMolecules(replacements, medicineMolecule);
    }

    /**
     * --- Part Two ---
     *
     * Now that the machine is calibrated, you're ready to begin molecule
     * fabrication.
     *
     * Molecule fabrication always begins with just a single electron, e, and
     * applying replacements one at a time, just like the ones during calibration.
     *
     * For example, suppose you have the following replacements:
     *
     * e => H
     * e => O
     * H => HO
     * H => OH
     * O => HH
     *
     * If you'd like to make HOH, you start with e, and then make the following
     * replacements:
     *
     *   - e => O to get O
     *   - O => HH to get HH
     *   - H => OH (on the second H) to get HOH
     *
     * So, you could make HOH after 3 steps. Santa's favorite molecule, HOHOHO,
     * can be made in 6 steps.
     *
     * How long will it take to make the medicine? Given the available
     * replacements and the medicine molecule in your puzzle input, what is the
     * fewest number of steps to go from e to the medicine molecule?
     *
     * Your puzzle answer was 212.
     */
    public static long findMedicine(Scanner scanner) {
        List<Pair<String, String>> rules = new ArrayList<>();
        String medicineMolecule = null;
        boolean readMolecule = false;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (StringUtils.isBlank(line)) {
                readMolecule = true;
                continue;
            }

            if (!readMolecule) {
                String[] split = line.split(" => ");
                rules.add(Pair.of(split[0], split[1]));
            } else {
                medicineMolecule = line;
            }
        }

        Objects.requireNonNull(medicineMolecule);

        Collections.reverse(rules);

        int count = 0;
        while (!"e".equals(medicineMolecule)) {
            for (Pair<String, String> rule : rules) {
                if ("e".equals(rule.left())) {
                    if (rule.right().equals(medicineMolecule)) {
                        medicineMolecule = "e";
                        count++;
                    }
                    continue;
                }
                if (medicineMolecule.contains(rule.right())) {
                    int position = medicineMolecule.lastIndexOf(rule.right());
                    medicineMolecule = medicineMolecule.substring(0, position) + rule.left() + medicineMolecule.substring(position + rule.right().length());
                    count++;
                }
            }
        }
        return count;
    }

    public static Set<List<String>> findNextMolecules(Map<String, List<List<String>>> replacements, List<String> medicineMolecule) {
        Set<List<String>> molecules = new HashSet<>();
        for (int i = 0; i < medicineMolecule.size(); i++) {
            String element = medicineMolecule.get(i);
            List<List<String>> replacement = replacements.get(element);
            if (replacement != null) {
                List<String> prefix = medicineMolecule.subList(0, i);
                List<String> suffix = medicineMolecule.subList(i + 1, medicineMolecule.size());
                for (List<String> r : replacement) {
                    List<String> newMolecule = new ArrayList<>();
                    newMolecule.addAll(prefix);
                    newMolecule.addAll(r);
                    newMolecule.addAll(suffix);
                    molecules.add(newMolecule);
                }
            }
        }
        return molecules;
    }
}
