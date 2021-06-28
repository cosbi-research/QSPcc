%---------------------------------------------------------------------
% Filename : MIST/data/extras/mat2csv.m
% Purpose  : Converts MIST-struct containing .mat file to CSV.
% Authors  : Chanchala Kaddi <chanchala.kaddi@sanofi.com>
%              Zach van Rijn <zachary.vanrijn@sanofi.com>
% License  : Company Internal
% Revision : 20170428
%---------------------------------------------------------------------

% MIST v1.0
%
% This function will convert any MIST-struct containing .mat file into
% a CSV file where each field of the struct is now a column in the CSV
% file. A header row with struct field names is also added.
%
% Usage:
%
%     mat2csv('../data/data_array.mat');
%
% Produces a file with the same name (now with '.csv' extension) in
% the same directory as the input file. No additional parameters are
% taken.
%
% TODO:
%
%   - parse the field names automatically and iterate over each field
%     in order, instead of having to explicitly state the fields in
%     numerous places.

function mat2csv(my_file)

    %% load the first (and hopefully only) structure within .mat file
    temp = whos('-file', my_file);
    load(my_file, '-mat');
    struct_name = eval(temp.name);

    %% get the number of fields (columns) and maximum number of rows
    num_fields = length(fieldnames(struct_name));
    num_max_el = max(structfun(@(field) numel(field), struct_name));
    csv_inputs = NaN(num_max_el, num_fields);

    %% assemble into matrix
    % Perhaps this can be rewritten to just loop over 1:num_fields (which
    % we already have) and parse the field names from the original struct?

    % simulation
    csv_inputs(1:numel(struct_name.simulation_settings)  ,  1) = struct_name.simulation_settings;
    csv_inputs(1:numel(struct_name.initial_conditions)   ,  2) = struct_name.initial_conditions;
    csv_inputs(1:numel(struct_name.dose_settings)        ,  3) = struct_name.dose_settings;
    csv_inputs(1:numel(struct_name.parameters)           ,  4) = struct_name.parameters;
    csv_inputs(1:numel(struct_name.calculated_parameters),  5) = struct_name.calculated_parameters;

    %% add header
    header = {
        'simulation_settings', ...
        'initial_conditions', ...
        'dose_settings', ...
        'parameters', ...
        'calculated_parameters', ...
    };

    %% determine file name
    [pathstr,name,~] = fileparts(my_file);
    filename = [pathstr '/' name '.csv'];

    % print to file
    fid = fopen(filename, 'w') ;
    fprintf(fid, '%s,%s,%s,%s,%s\n', header{1:end}) ;
    fclose(fid);

    dlmwrite(filename, csv_inputs, '-append')

end