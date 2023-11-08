import csv
import matplotlib.pyplot as plt

# Initialize lists to hold the wall time and throughput values
wall_times = []
throughputs = []

# Open the CSV file and read the contents
with open('step6.csv', 'r') as csvfile:
    plots = csv.reader(csvfile, delimiter=',')
    for row in plots:
        wall_times.append(int(row[0]))  # Assuming the first column contains integers
        throughputs.append(int(row[1]))  # Assuming the second column contains integers

# Plot the data with a smaller marker size
plt.figure(figsize=(10, 5))
plt.plot(wall_times, throughputs, marker='o', markersize=3)  # Adjust markersize to make points smaller

# Add title and labels
plt.title('Wall Time vs Throughput')
plt.xlabel('Wall Time')
plt.ylabel('Throughput')

# Show grid
plt.grid(True)

# Show the plot
plt.show()
