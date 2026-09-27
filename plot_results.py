import csv
import os

import matplotlib.pyplot as plt


RESULTS_FILE = "results/results.csv"
PLOTS_DIR = "docs/plots"


def read_results():
    data = []

    with open(RESULTS_FILE, "r", newline="", encoding="utf-8") as file:
        reader = csv.DictReader(file)

        for row in reader:
            data.append({
                "algorithm": row["algorithm"],
                "input_type": row["input_type"],
                "n": int(row["n"]),
                "time_ns": int(row["time_ns"]),
                "recursion_depth": int(row["recursion_depth"])
            })

    return data


def plot_time(data):
    algorithms = [
        "MergeSort",
        "QuickSort",
        "DeterministicSelect",
        "ClosestPair"
    ]

    plt.figure(figsize=(10, 6))

    for algorithm in algorithms:

        rows = [
            row for row in data
            if row["algorithm"] == algorithm
               and row["input_type"] == "random"
        ]

        rows.sort(key=lambda row: row["n"])

        x = [row["n"] for row in rows]
        y = [row["time_ns"] / 1_000_000 for row in rows]

        plt.plot(
            x,
            y,
            marker="o",
            label=algorithm
        )

    plt.xlabel("Input size (n)")
    plt.ylabel("Execution time (ms)")
    plt.title("Execution Time vs. Input Size")
    plt.legend()
    plt.grid(True, alpha=0.3)

    plt.tight_layout()

    output = os.path.join(
        PLOTS_DIR,
        "time_vs_n.png"
    )

    plt.savefig(output, dpi=200)
    plt.close()

    print("Created:", output)


def plot_recursion_depth(data):
    algorithms = [
        "MergeSort",
        "QuickSort",
        "DeterministicSelect",
        "ClosestPair"
    ]

    plt.figure(figsize=(10, 6))

    for algorithm in algorithms:

        rows = [
            row for row in data
            if row["algorithm"] == algorithm
               and row["input_type"] == "random"
        ]

        rows.sort(key=lambda row: row["n"])

        x = [row["n"] for row in rows]
        y = [row["recursion_depth"] for row in rows]

        plt.plot(
            x,
            y,
            marker="o",
            label=algorithm
        )

    plt.xlabel("Input size (n)")
    plt.ylabel("Maximum recursion depth")
    plt.title("Recursion Depth vs. Input Size")
    plt.legend()
    plt.grid(True, alpha=0.3)

    plt.tight_layout()

    output = os.path.join(
        PLOTS_DIR,
        "recursion_depth_vs_n.png"
    )

    plt.savefig(output, dpi=200)
    plt.close()

    print("Created:", output)


def main():
    os.makedirs(PLOTS_DIR, exist_ok=True)

    data = read_results()

    print("Loaded", len(data), "experimental results.")

    plot_time(data)
    plot_recursion_depth(data)

    print()
    print("All plots created successfully.")


if __name__ == "__main__":
    main()