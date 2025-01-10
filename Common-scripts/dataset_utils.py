import csv
import os

"""
Script to manipulate datasets
"""

def merge_partitioned_files(input_folder: str, output_file: str):
    # Initialize a list to store the data from all CSV files
    all_data = []
    # Loop through each CSV file in the input folder
    for filename in os.listdir(input_folder):
        if filename.endswith('.csv'):
            file_path = os.path.join(input_folder, filename)

            # Open and read the current CSV file, handling bad lines
            with open(file_path, 'r', newline='', encoding='utf-8', errors='ignore') as csv_file:
                reader = csv.reader(csv_file)

                # Skip the header in all files except the first one
                if len(all_data) > 0:
                    next(reader)

                # Append the rows to the list, handling bad lines
                try:
                    all_data.extend(list(reader))
                except csv.Error:
                    # Handle the bad line, or simply skip it
                    pass

    print(len(all_data))
    # Write the merged data to the output file
    with open(output_file, 'w', newline='', encoding='utf-8') as merged_file:
        writer = csv.writer(merged_file)

        # Write the header from the first CSV file
        writer.writerow(all_data[0])

        # Write the remaining rows
        writer.writerows(all_data[1:])


def extract_first_column(input_csv, output_csv):
    try:
        with open(input_csv, 'r', newline='', errors='ignore') as infile, open(output_csv, 'w', newline='') as outfile:
            reader = csv.reader(infile)
            writer = csv.writer(outfile)

            for row in reader:
                if row:  # Check if the row is not empty
                    try:
                        first_column = row[0]
                        writer.writerow([first_column])
                    except IndexError:
                        # Handle or skip lines with fewer columns
                        pass

        print(f'Extracted the first column from {input_csv} and saved to {output_csv}')
    except Exception as e:
        print(f'An error occurred: {str(e)}')


# Define a function to create a subset CSV file
def create_subset_csv(input_file, output_file, n_lines: int):
    with open(input_file, 'r', newline='') as infile, open(output_file, 'w', newline='') as outfile:
        reader = csv.reader(infile)
        writer = csv.writer(outfile)

        # Read and store the first N lines as criteria
        criteria_lines = [next(reader) for _ in range(n_lines)]

        # Write the first N lines to the output CSV
        writer.writerows(criteria_lines)

        # Filter and write rows based on the criteria
        for row in reader:
            if row[:n_lines] == criteria_lines[0]:
                writer.writerow(row)


if __name__ == "__main__":
    print("")

